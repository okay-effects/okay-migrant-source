package okay.migrant

import okay.effects.json.Json

import java.nio.file.{Files, Path}
import scala.util.Random

/** THE CUKR BOOK WRITTEN: the lesson (the information sheet and the official answers 1–10, as published) and
 * sixty sampled exercises, into `books/pl/cukr/` — what a teacher studies (okay-study's `Judge`) */
object CukrBook:
  import Cukr.sampled

  /** the lesson: the information sheet and the official answers 1–10, as published (page footers taken off) */
  def lesson: String =
    def clean(file: String): Vector[String] = Files.readString(Path.of("books/pl/cukr", file)).linesIterator.toVector
      .filterNot(l => l.contains("Taborowa") || l.contains("02-699") || l.trim == "rodo" || l.contains('\f'))
    val faq = clean("faq-pl.txt")
    val upTo = faq.indexWhere(_.trim.startsWith("11. Jestem obywatelem Ukrainy"))
    (clean("info-pl.txt") ++ Vector("", "PYTANIA I ODPOWIEDZI (1–10)", "") ++ faq.take(upTo)).mkString("\n")

  def main(args: Array[String]): Unit =
    val rnd = Random(20261119)
    val exercises = (1 to 60).toVector.map(sampled(_, rnd))
    val dir = Path.of("books/pl/cukr")
    Files.writeString(dir.resolve("lesson.txt"), lesson)
    Files.writeString(dir.resolve("exercises.jsonl"), exercises.map(e =>
      Json.obj("number" -> Json.num(e.number), "sentences" -> Json.Arr(e.sentences.map(Json.Str(_))),
        "answer" -> Json.Str(e.answer)).render).mkString("\n") + "\n")
    val kinds = exercises.groupBy(_.answer).view.mapValues(_.size).toVector.sortBy(-_._2)
    println(s"${exercises.size} exercises; answers: ${kinds.take(8).map((a, n) => s"$n $a").mkString("; ")}")
