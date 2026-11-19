package okay.migrant

import okay.scala3.*

/** THE PERSON, as the assistant's program sees them: asked a question, they choose one of its answers or type a
 * value. Who answers — a person at a page, a console, a script in a test — is the handler's */
enum Person[X]:
  case Ask(question: Question) extends Person[Person.Said]

object Person:
  enum Said:
    case Chose(choice: String)
    case Typed(value: String)

  def ask[R[_]](q: Question)(using In[Person, R]): Said ! R = perform[Person](Ask(q))

  /** a person played by answers kept by the question's id; a question with no answer there is an error — the
   * program may not go on without asking, and a test may not answer what was not asked */
  def scripted[R[_], Rest[_], A](answers: Map[String, Said], asked: collection.mutable.Buffer[String])(p: A ! R)(
      using Split[Person, R, Rest]): A ! Rest =
    handle(p, Handler.tail[Person, Rest, A, A](pure)([X] => (op: Person[X]) => op match
      case Ask(q) =>
        asked += q.id
        answers.getOrElse(q.id, sys.error(s"asked ${q.id}, and the script has no answer"))))

/** what the assistant concludes: the case as stated, the model's verdicts, and — in words — each verdict with
 * its official source, what to remember, the book and its day */
final case class Outcome(sentences: Vector[String], verdicts: Vector[String], asked: Vector[String]):
  def eligible: Boolean = verdicts == Vector("eligible")

  def said(topic: Topic, language: String): String =
    def quote(s: Source): String =
      val q = s.quotes.getOrElse(language, s.quotes.getOrElse("pl", s.quotes.values.head))
      s"    «$q» — ${s.place}"
    val lines = verdicts.flatMap(v => topic.reasons.get(v).map(r => s"• ${r.text.in(language)}\n${quote(r.source)}"))
    val more = if eligible then topic.remember.map(r => s"! ${r.text.in(language)}\n${quote(r.source)}") else Vector.empty
    (lines ++ more :+ s"${topic.book.in(language)} (${topic.fetched}). ${Interview.notAdvice.in(language)}")
      .mkString("\n")

object Interview:
  val notAdvice: Words = Words(
    "Це інформація з офіційних матеріалів, а не юридична консультація.",
    "Это информация из официальных материалов, а не юридическая консультация.",
    "To informacja z oficjalnych materiałów, a nie porada prawna.",
    "This is information from official materials, not legal advice.")

  /** THE CONVERSATION: while an answer can still change the model's verdict, the question it belongs to is asked;
   * then the verdicts. Nothing is assumed: a fact is in the case only because the person said it */
  def run[R[_]: Has[Person]](topic: Topic): Outcome ! R =
    loop[(Map[String, String], Vector[String]), Outcome, R]((Map.empty, Vector.empty)) { (known, asked) =>
      topic.next(known) match
        case None =>
          pure(Right(Outcome(known.values.toVector.sorted, topic.verdicts(known.values.toSet).toVector.sorted, asked)))
        case Some(q) =>
          Person.ask(q).map { said =>
            val sentence = said match
              case Person.Said.Chose(id) => q.choices.find(_.id == id).map(_.sentence)
              case Person.Said.Typed(value) => q.typed.flatMap(_(value))
            // an answer that is none of the question's: the question is asked again
            val state = sentence.fold((known, asked))(s => (known + (q.id -> s), asked :+ q.id))
            Left(state): Either[(Map[String, String], Vector[String]), Outcome]
          }
    }
