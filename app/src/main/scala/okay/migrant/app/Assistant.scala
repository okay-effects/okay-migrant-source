package okay.migrant.app

import okay.business.scala3.*
import okay.migrant.{CukrTopic, Interview, Models, Outcome, Person, Question, Topic}
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

  /** the model a teacher wrote from the official lesson, checked against the office's cases (models/pl-cukr.json) */
  def cukr(today: Int): Topic = CukrTopic.topic(Model.parse(Models.plCukr).fold(sys.error, identity), today)

  private def asked[R[_]: Has[Ui]](topic: Topic, language: String)(q: Question): Person.Said ! R =
    show(Screen[Person.Said](topic.name.in(language))(Part.section(q.text.in(language))())(
      q.choices.map(c => Button(c.label.in(language))(Person.Said.Chose(c.id)))*))

  private def answered[R[_]: Has[Ui]](topic: Topic, language: String, outcome: Outcome): Unit ! R =
    val w = words(language)
    def source(place: String, quotes: Map[String, String]): Part =
      Part.text("«" + quotes.getOrElse(language, quotes.getOrElse("pl", quotes.values.head)) + "» — " + place)
    val verdicts = outcome.verdicts.flatMap(v => topic.reasons.get(v)).flatMap(r =>
      Vector(if outcome.eligible then Part.info(r.text.in(language)) else Part.problem(r.text.in(language)),
        source(r.source.place, r.source.quotes)))
    val remember = if !outcome.eligible then Vector.empty else topic.remember.flatMap(r =>
      Vector(Part.warning(r.text.in(language)), source(r.source.place, r.source.quotes)))
    val foot = Part.text(s"${topic.book.in(language)} ${topic.fetched}. ${Interview.notAdvice.in(language)}")
    val title = if outcome.eligible then w("Можна подавати", "Можно подавать", "Można składać", "You may apply")
      else w("Зараз подати не вдасться", "Сейчас подать не получится", "Teraz nie można złożyć",
        "The application cannot be made now")
    show(Screen[Unit](title)((verdicts ++ remember :+ foot)*)(
      Button(w("Ще раз", "Ещё раз", "Jeszcze raz", "Again"), Style.Primary)(())))

  /** the page, for ever: a language, a conversation, an answer, and again */
  def main[R[_]: Has[Ui]](today: Int): Unit ! R =
    menu[String, R]("CUKR", languages*).flatMap { language =>
      val topic = cukr(today)
      Interview.by[R](topic)(asked[R](topic, language)).flatMap(answered[R](topic, language, _))
    }.andThen(defer(main[R](today)))
