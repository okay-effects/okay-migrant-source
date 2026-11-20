package okay.migrant.app

import okay.business.scala3.*
import okay.migrant.{ArrivalTopic, CukrAfterTopic, CukrTopic, Interview, Models, Outcome, PeselTopics, Person,
  Question, ResidenceTopics, Topic, Words}
import okay.study.Model

/** THE ASSISTANT'S PAGE AS A PROGRAM OF `Ui` ALONE (okay-ui): the language, then the conversation — a question
 * a screen, an answer a button — then the answer with the office's own sentences, and again. What a screen looks
 * like is the toolkit's; a test plays the same program by its buttons' labels */
object Assistant:
  val languages: Vector[(String, String)] =
    Vector("Українська" -> "uk", "Русский" -> "ru", "Polski" -> "pl", "English" -> "en")

  private def words(language: String)(uk: String, ru: String, pl: String, en: String): String = language match
    case "uk" => uk
    case "ru" => ru
    case "pl" => pl
    case _ => en

  /** the topics: may I apply (models/pl-cukr.json), and what follows (models/pl-cukr-after.json) — each model
   * written by a teacher from the official text and checked against the office's cases */
  def topics(today: Int): Vector[Topic] =
    CukrTopic.topic(Model.parse(Models.plCukr).fold(sys.error, identity), today) +:
      CukrAfterTopic.topics(Model.parse(Models.plCukrAfter).fold(sys.error, identity))

  /** AN AREA: a kind of permit and the topics of it; an area of one topic is that topic */
  final case class Area(id: String, name: Words, topics: Vector[Topic])

  private def model(text: String) = Model.parse(text).fold(sys.error, identity)

  /** what the page knows, in Poland: the CUKR card (eight topics), PESEL (three), the single permit, permanent
   * residence, the EU resident */
  def areas(today: Int): Vector[Area] = Vector(
    Area("cukr", Words("Посвідка CUKR (для громадян України зі статусом UKR)",
      "Карта CUKR (для граждан Украины со статусом UKR)",
      "Karta pobytu CUKR (dla obywateli Ukrainy ze statusem UKR)",
      "The CUKR card (for Ukrainians with the UKR status)"),
      topics(today)),
    Area("arrival", ArrivalTopic.topic(model(Models.plArrival)).name,
      Vector(ArrivalTopic.topic(model(Models.plArrival)))),
    Area("pesel", Words("PESEL і PESEL UKR", "PESEL и PESEL UKR", "PESEL i PESEL UKR", "PESEL and PESEL UKR"),
      PeselTopics.topics(model(Models.plPesel))),
    Area("work", ResidenceTopics.work(model(Models.plWork)).name, Vector(ResidenceTopics.work(model(Models.plWork)))),
    Area("permanent", ResidenceTopics.permanent(model(Models.plPermanent)).name,
      Vector(ResidenceTopics.permanent(model(Models.plPermanent)))),
    Area("eu-resident", ResidenceTopics.euResident(model(Models.plEuResident)).name,
      Vector(ResidenceTopics.euResident(model(Models.plEuResident)))))

  /** a topic's place in an address: its English name, as words of a link (`can-i-apply-for-the-cukr-card`) */
  def slug(topic: Topic): String = topic.name.en.toLowerCase.replaceAll("[^a-z0-9]+", "-").stripPrefix("-")
    .stripSuffix("-")

  /** the answers given, as an address's segments `question=choice`, in the topic's order of questions */
  private def answeredSoFar(topic: Topic, known: Map[String, String]): Vector[String] =
    topic.questions.flatMap(q => known.get(q.id).flatMap(s => q.choices.find(_.sentence == s))
      .map(c => s"${q.id}=${c.id}"))

  private def asked[R[_]: Has[Ui]: Has[Address]](topic: Topic, language: String, at: Vector[String],
      opened: Map[String, String])(known: Map[String, String], q: Question): Person.Said ! R =
    opened.get(q.id).filter(id => q.choices.exists(_.id == id)) match
      // a link's answer: given, not asked
      case Some(id) => pure(Person.Said.Chose(id))
      case None => nowAt[R]((at ++ answeredSoFar(topic, known)).toList).andThen(
        show(Screen[Person.Said](topic.name.in(language))(Part.section(q.text.in(language))())(
          q.choices.map(c => Button(c.label.in(language))(Person.Said.Chose(c.id)))*)))

  private def answered[R[_]: Has[Ui]: Has[Address]](topic: Topic, language: String, at: Vector[String],
      outcome: Outcome, named: Vector[String] => Option[String]): Vector[String] ! R =
    val w = words(language)
    def sources(r: okay.migrant.Reason): Vector[Part] = r.sources.map(s => Part.text(Outcome.quoted(s, language)))
    val good = outcome.good(topic)
    val verdicts = outcome.verdicts.flatMap(v => topic.reasons.get(v)).flatMap(r =>
      (if r.good then Part.info(r.text.in(language)) else Part.problem(r.text.in(language))) +: sources(r))
    val remember = if !good then Vector.empty else topic.remember.flatMap(r =>
      Part.warning(r.text.in(language)) +: sources(r))
    val foot = Part.text(s"${topic.book.in(language)} ${topic.fetched}. ${Interview.notAdvice.in(language)}")
    val known = outcome.asked.flatMap(id => topic.questions.find(_.id == id)).flatMap(q =>
      q.choices.find(c => outcome.sentences.contains(c.sentence)).map(c => q.id -> c.sentence)).toMap
    // a step that the assistant says more of: a button to that topic (its address after the language)
    val more = outcome.verdicts.flatMap(topic.reasons.get).map(_.see).filter(_.nonEmpty).distinct
      .flatMap(see => named(see).map(name =>
        Button(w("Детальніше: ", "Подробнее: ", "Więcej: ", "More: ") + name)(see)))
    nowAt[R]((at ++ answeredSoFar(topic, known)).toList).andThen(
      show(Screen[Vector[String]](topic.title(good).in(language))((verdicts ++ remember :+ foot)*)((more :+
        Button(w("Ще раз", "Ещё раз", "Jeszcze raz", "Again"), Style.Primary)(Vector.empty[String]))*)))

  /** a menu at its address: the address said, then the menu — unless the address opened already chose */
  private def chosen[A, R[_]: Has[Ui]: Has[Address]](at: Vector[String], opened: Option[A], title: String,
      options: (String, A)*): A ! R =
    opened.fold(nowAt[R](at.toList).andThen(menu[A, R](title, options*)))(pure(_))

  /** the page, for ever: a language, a situation, its conversation, an answer, and again. Every screen has its
   * address — the language, the area, the topic, the answers given — and the page opened at an address goes
   * straight to its screen */
  def main[R[_]: Has[Ui]: Has[Address]](today: Int): Unit ! R =
    whereOpened[R].flatMap(path => from[R](today, path.toVector))

  private def from[R[_]: Has[Ui]: Has[Address]](today: Int, path: Vector[String]): Unit ! R =
    // a topic's name by its address after the language: the area's id, and the topic's words when the area has many
    def named(language: String)(see: Vector[String]): Option[String] =
      areas(today).find(a => see.headOption.contains(a.id)).flatMap(a =>
        if a.topics.size == 1 then Some(a.topics.head.name.in(language))
        else a.topics.find(t => see.lift(1).contains(slug(t))).map(_.name.in(language)))
    val all = areas(today)
    val language = path.headOption.filter(l => languages.exists(_._2 == l))
    chosen[String, R](Vector.empty, language, "Polska · Польща · Польша · Poland", languages*).flatMap { language =>
      val rest = if path.headOption.contains(language) then path.drop(1) else Vector.empty
      val what = words(language)("Що вас цікавить?", "Что вас интересует?", "Co Cię interesuje?",
        "What do you want to know?")
      val area = rest.headOption.flatMap(id => all.find(_.id == id))
      chosen[Area, R](Vector(language), area, what, all.map(a => a.name.in(language) -> a)*).flatMap { area =>
        val more = if rest.headOption.contains(area.id) then rest.drop(1) else Vector.empty
        val (topic, answers) =
          if area.topics.size == 1 then (Some(area.topics.head), more)
          else more.headOption.flatMap(id => area.topics.find(slug(_) == id)).fold((None, Vector.empty))(t =>
            (Some(t), more.drop(1)))
        chosen[Topic, R](Vector(language, area.id), topic, area.name.in(language),
          area.topics.map(t => t.name.in(language) -> t)*).flatMap { topic =>
          val at = Vector(language, area.id) ++ Option.when(area.topics.size > 1)(slug(topic))
          val opened = answers.flatMap(_.split("=", 2) match
            case Array(q, c) => Some(q -> c)
            case _ => None).toMap
          Interview.along[R](topic)(asked[R](topic, language, at, opened))
            .flatMap(answered[R](topic, language, at, _, named(language)))
            .map(see => if see.isEmpty then Vector.empty else language +: see)
        }
      }
    }.flatMap(next => defer(from[R](today, next)))
