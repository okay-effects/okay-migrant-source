package okay.migrant

import okay.scala3.*
import okay.study.{Exercise, Model}

import java.nio.file.{Files, Path}

/** the conversation over the CUKR card: the office's cases told by answering what is asked, and the law of
 * quotes — every official sentence the assistant shows stands in the book as published */
class InterviewSuite extends munit.FunSuite:
  private def text(path: String) = Files.readString(Path.of(path))
  private val model = Model.parse(text("models/pl-cukr.json")).fold(e => fail(e), identity)
  private val topic = CukrTopic.topic(model, today = 20261119)

  /** the answers a case's sentences give to the questions; a question the case says nothing to has none */
  private def script(e: Exercise): Map[String, Person.Said] =
    val Days = """the UKR status has lasted (\d+) days without a break""".r
    val Made = """the application is made on (\d{4})(\d{2})(\d{2})""".r
    topic.questions.flatMap { q =>
      val said = e.sentences.collectFirst {
        case Days(n) if q.id == "days" => Person.Said.Chose(if n.toInt >= 365 then "enough" else "short")
        case Made(y, m, d) if q.id == "date" => Person.Said.Typed(s"$d.$m.$y")
        case s if q.id != "date" && q.choices.exists(_.sentence == s) =>
          Person.Said.Chose(q.choices.find(_.sentence == s).get.id)
      }
      said.map(q.id -> _)
    }.toMap

  private def told(e: Exercise): Outcome =
    val asked = collection.mutable.ArrayBuffer.empty[String]
    run(Person.scripted(script(e), asked)(Interview.run[Person](topic)))

  for (what, e) <- Cukr.official do
    test(s"told through the conversation, official question $what"):
      val outcome = told(e)
      assertEquals(outcome.verdicts, e.answer.split(" \\+ ").toVector.sorted, outcome.asked.mkString(", "))

  test("only what can change the answer is asked"):
    val child = told(Cukr.official(5)._2)   // a child born in Poland
    assert(!child.asked.contains("pesel_june") && !child.asked.contains("days"), child.asked)
    val adult = told(Cukr.official(1)._2)
    assert(!adult.asked.contains("mother"), adult.asked)
    assertEquals(adult.asked.head, "who")

  test("every official sentence shown stands in the book, as published"):
    def plain(s: String) = s.replaceAll("[\\s\\u00a0\\uf0b7•]+", " ").trim
    val books = Map("pl" -> Vector("info-pl.txt", "faq-pl.txt"), "uk" -> Vector("info-ua.txt", "faq-ua.txt"))
      .view.mapValues(fs => plain(fs.map(f => text(s"books/pl/cukr/$f")).mkString(" "))).toMap
    val missing = for
      reason <- topic.reasons.values.toVector ++ topic.remember
      (language, quote) <- reason.source.quotes.toVector
      if !books(language).contains(plain(quote))
    yield s"$language: $quote"
    assertEquals(missing, Vector.empty[String])

  test("an answer is said with its source, the day of the book and what it is not"):
    val said = told(Cukr.official(2)._2).said(topic, "ru")
    assert(said.contains("365"), said)
    assert(said.contains("2026-11-19") && said.contains("не юридическая консультация"), said)
    println(said)
    println("---")
    println(told(Cukr.official(1)._2).said(topic, "uk"))
