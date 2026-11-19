package okay.migrant.app

import okay.business.scala3.*
import okay.migrant.{CukrAfterTopic, CukrTopic, Interview, Models, Outcome, Person, Question, Topic}
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

  private def asked[R[_]: Has[Ui]](topic: Topic, language: String)(q: Question): Person.Said ! R =
    show(Screen[Person.Said](topic.name.in(language))(Part.section(q.text.in(language))())(
      q.choices.map(c => Button(c.label.in(language))(Person.Said.Chose(c.id)))*))

  private def answered[R[_]: Has[Ui]](topic: Topic, language: String, outcome: Outcome): Unit ! R =
    val w = words(language)
    def sources(r: okay.migrant.Reason): Vector[Part] = r.sources.map(s => Part.text(Outcome.quoted(s, language)))
    val good = outcome.good(topic)
    val verdicts = outcome.verdicts.flatMap(v => topic.reasons.get(v)).flatMap(r =>
      (if r.good then Part.info(r.text.in(language)) else Part.problem(r.text.in(language))) +: sources(r))
    val remember = if !good then Vector.empty else topic.remember.flatMap(r =>
      Part.warning(r.text.in(language)) +: sources(r))
    val foot = Part.text(s"${topic.book.in(language)} ${topic.fetched}. ${Interview.notAdvice.in(language)}")
    show(Screen[Unit](topic.title(good).in(language))((verdicts ++ remember :+ foot)*)(
      Button(w("Ще раз", "Ещё раз", "Jeszcze raz", "Again"), Style.Primary)(())))

  /** the page, for ever: a language, a situation, its conversation, an answer, and again */
  def main[R[_]: Has[Ui]](today: Int): Unit ! R =
    menu[String, R]("CUKR", languages*).flatMap { language =>
      val all = topics(today)
      val what = words(language)("Що вас цікавить?", "Что вас интересует?", "Co Cię interesuje?",
        "What do you want to know?")
      menu[Topic, R](what, all.map(t => t.name.in(language) -> t)*).flatMap(topic =>
        Interview.by[R](topic)(asked[R](topic, language)).flatMap(answered[R](topic, language, _)))
    }.andThen(defer(main[R](today)))
