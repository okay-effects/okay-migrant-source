package okay.migrant

import okay.study.{Model, Solve}

/** a text in the people's languages: uk, ru, pl, en */
final case class Words(uk: String, ru: String, pl: String, en: String):
  def in(language: String): String = language match
    case "uk" => uk
    case "ru" => ru
    case "pl" => pl
    case _ => en

/** an answer a person may give, and the canonical sentence of the case it states */
final case class Choice(id: String, label: Words, sentence: String)

/** A QUESTION ABOUT A PERSON'S CASE: what is asked, the answers offered — each a canonical sentence — and, for a
 * question whose answer is a value (a date), how a typed value becomes its sentence (the offered answers then
 * stand for the kinds of value: before, within, after) */
final case class Question(id: String, text: Words, choices: Vector[Choice],
    typed: Option[String => Option[String]] = None)

/** WHERE A CONCLUSION COMES FROM: the official sentence it rests on, quoted as published in the languages the
 * office published it in, and where it stands in the book */
final case class Source(place: String, quotes: Map[String, String])

/** what the assistant says for a verdict: its plain wording (ours, unofficial) and the official source */
final case class Reason(text: Words, source: Source)

/** A TOPIC THE ASSISTANT KNOWS: the model checked against the office's cases, the questions that state a case,
 * what each verdict means and rests on, what is to be remembered beside a positive answer, and the book */
final case class Topic(name: Words, model: Model, questions: Vector[Question], reasons: Map[String, Reason],
    remember: Vector[Reason], book: Words, fetched: String):

  private val cache = scala.collection.mutable.HashMap.empty[Set[String], Set[String]]

  /** the model's verdicts for a case's sentences (the exact side; a case the model cannot run has none) */
  def verdicts(sentences: Set[String]): Set[String] =
    cache.getOrElseUpdate(sentences, Solve.run(model, sentences.toVector.sorted).fold(_ => Set.empty, _.map(_._2).toSet))

  /** every answer the model may still give, over all the ways the unanswered questions may be answered */
  def open(known: Map[String, String]): Set[Set[String]] =
    val rest = questions.filterNot(q => known.contains(q.id))
    rest.foldLeft(Vector(known.values.toSet))((cases, q) => cases.flatMap(c => q.choices.map(c + _.sentence)))
      .map(verdicts).toSet

  /** THE NEXT QUESTION: the first one whose answer can still change what the model answers — a question that
   * cannot is never asked (a child born in Poland is not asked about 4 June 2025); none: the case is decided */
  def next(known: Map[String, String]): Option[Question] =
    if open(known).size <= 1 then None
    else questions.filterNot(q => known.contains(q.id))
      .find(q => q.choices.map(c => open(known + (q.id -> c.sentence))).distinct.size > 1)
