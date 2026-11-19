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
      .filterNot(l => l.contains("Taborowa") || l.contains("02-699") || l.trim == "rodo").map(_.replace("\f", ""))
    val faq = clean("faq-pl.txt")
    val upTo = faq.indexWhere(_.trim.startsWith("11. Jestem obywatelem Ukrainy"))
    (clean("info-pl.txt") ++ Vector("", "PYTANIA I ODPOWIEDZI (1–10)", "") ++ faq.take(upTo)).mkString("\n")

  /** the second lesson: the official answers 2–5, 9 and 43–60, as published */
  def lessonAfter: String =
    val faq = Files.readString(Path.of("books/pl/cukr", "faq-pl.txt")).linesIterator.toVector
      .filterNot(l => l.contains("Taborowa") || l.contains("02-699") || l.trim == "rodo").map(_.replace("\f", ""))
    def from(start: String, end: String): Vector[String] =
      val a = faq.indexWhere(_.trim.startsWith(start)); val b = faq.indexWhere(_.trim.startsWith(end), a + 1)
      require(a >= 0 && b > a, s"$start … $end")
      faq.slice(a, b)
    (from("2. Dlaczego warto", "6. Kto może") ++ from("9. O czym muszę", "10. Czy na dzień") ++
      faq.drop(faq.indexWhere(_.contains("43. Czy po")).ensuring(_ >= 0))).mkString("\n")

  private def written(dir: Path, text: String, exercises: Vector[okay.study.Exercise]): Unit =
    Files.createDirectories(dir)
    Files.writeString(dir.resolve("lesson.txt"), text)
    Files.writeString(dir.resolve("exercises.jsonl"), exercises.map(e =>
      Json.obj("number" -> Json.num(e.number), "sentences" -> Json.Arr(e.sentences.map(Json.Str(_))),
        "answer" -> Json.Str(e.answer)).render).mkString("\n") + "\n")

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
    // the second book: the same sources (books/pl/cukr), its own lesson and cases
    // the cases shown to a teacher must hold every word an answer is made of (a verdict's name cannot be guessed):
    // first the fewest cases that do, twice over, then the rest
    val drawn = { val r = Random(20261120); (1 to 90).toVector.map(CukrAfter.sampled(_, r)).distinctBy(_.sentences) }
    def words(e: okay.study.Exercise) = e.answer.split(" \\+ ").toSet
    def covering(from: Vector[okay.study.Exercise]): Vector[okay.study.Exercise] =
      val all = from.flatMap(words).toSet
      Iterator.unfold((Set.empty[String], from)) { (seen, rest) =>
        Option.when(seen != all)(rest.maxBy(e => (words(e) -- seen).size)).map(e => e -> (seen ++ words(e),
          rest.filter(_ != e)))
      }.toVector
    val first = covering(drawn)
    val second = covering(drawn.filterNot(first.contains))
    val shown = first ++ second
    val after = (shown ++ drawn.filterNot(shown.contains)).zipWithIndex.map((e, i) => e.copy(number = i + 1))
    written(Path.of("books/pl/cukr-after"), lessonAfter, after)
    println(s"after: ${after.size} exercises, ${after.map(_.answer).distinct.size} different answers; " +
      s"the first ${shown.size} hold every verdict twice (TRAIN=${shown.size})")
