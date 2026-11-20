package okay.migrant

import okay.effects.json.Json
import okay.study.Exercise

import java.nio.file.{Files, Path}
import scala.util.Random

/** A BOOK WRITTEN FOR A TEACHER: its lesson (cut from the sources as published) and its exercises, the ones to
 * show first. The cases shown must hold every word an answer is made of — a verdict's name cannot be guessed —
 * so first the fewest cases that do, twice over, then the rest. Returns how many to show (`TRAIN`) */
object Books:
  def write(dir: Path, lesson: String, drawn: Vector[Exercise]): Int =
    // what a teacher must see: every verdict's name, and every kind of sentence (its numbers aside)
    def words(e: Exercise) = e.answer.split(" \\+ ").toSet ++ e.sentences.map(_.replaceAll("[0-9]+", "#"))
    def covering(from: Vector[Exercise]): Vector[Exercise] =
      val all = from.flatMap(words).toSet
      Iterator.unfold((Set.empty[String], from)) { (seen, rest) =>
        Option.when(seen != all)(rest.maxBy(e => (words(e) -- seen).size)).map(e => e -> (seen ++ words(e),
          rest.filter(_ != e)))
      }.toVector
    val distinct = drawn.distinctBy(_.sentences.toSet)
    val first = covering(distinct)
    val covered = first ++ covering(distinct.filterNot(first.contains))
    // at least twelve: a threshold is learnt from cases on both of its sides
    val shown = covered ++ distinct.filterNot(covered.contains).take(12 - covered.size)
    val all = (shown ++ distinct.filterNot(shown.contains)).zipWithIndex.map((e, i) => e.copy(number = i + 1))
    Files.createDirectories(dir)
    Files.writeString(dir.resolve("lesson.txt"), lesson)
    Files.writeString(dir.resolve("exercises.jsonl"), all.map(e =>
      Json.obj("number" -> Json.num(e.number), "sentences" -> Json.Arr(e.sentences.map(Json.Str(_))),
        "answer" -> Json.Str(e.answer)).render).mkString("\n") + "\n")
    println(s"$dir: ${all.size} exercises, ${all.map(_.answer).distinct.size} different answers; " +
      s"the first ${shown.size} hold every verdict and every kind of sentence twice (TRAIN=${shown.size})")
    shown.size

  /** the lines of a source's text from the heading `from` to the one before the heading `to` — a heading, not
   * the page's menu entry of the same word: the line after it is text */
  def between(file: Path, from: String, to: String): Vector[String] =
    val lines = Files.readString(file).linesIterator.toVector
    def heading(i: Int) = lines.lift(i + 1).exists(_.trim.length > 40)
    val a = lines.indices.find(i => lines(i).trim == from && heading(i)).getOrElse(-1)
    val b = lines.indices.find(i => i > a && lines(i).trim == to && heading(i)).getOrElse(-1)
    require(a >= 0 && b > a, s"$file: $from … $to")
    lines.slice(a, b)

/** THE TWO RESIDENCE BOOKS WRITTEN: the lesson is the information page's introduction and requirements and the
 * official questions and answers, in Polish, as published; the exercises sampled */
object ResidenceBooks:
  def main(args: Array[String]): Unit =
    val permanent = Path.of("books/pl/permanent")
    val faqStart = "Posiadam Kartę Polaka ile lat muszę zamieszkiwać w Polsce aby uzyskać prawo stałego pobytu?"
    val faq = Files.readString(permanent.resolve("faq-pl.txt")).linesIterator.toVector
    val qa = faq.drop(faq.indexWhere(_.trim == faqStart).ensuring(_ >= 0))
      .takeWhile(l => !l.contains("ciasteczek"))
    Books.write(permanent.resolve("study"),
      (Books.between(permanent.resolve("info-pl.txt"), "Wprowadzenie", "Dokumenty") ++
        Vector("", "PYTANIA I ODPOWIEDZI", "") ++ qa).mkString("\n"),
      { val r = Random(20261120); (1 to 120).toVector.map(Permanent.sampled(_, r)) })
    val eu = Path.of("books/pl/eu-resident")
    val euFaq = Files.readString(eu.resolve("faq-pl.txt")).linesIterator.toVector
    val euQa = euFaq.drop(euFaq.indexWhere(_.contains("Jakie musz")).ensuring(_ >= 0))
      .takeWhile(l => !l.contains("ciasteczek"))
    Books.write(eu.resolve("study"),
      (Books.between(eu.resolve("info-pl.txt"), "Wprowadzenie", "Dokumenty") ++
        Vector("", "PYTANIA I ODPOWIEDZI", "") ++ euQa).mkString("\n"),
      { val r = Random(20261121); (1 to 120).toVector.map(EuResident.sampled(_, r)) })

    val work = Path.of("books/pl/work")
    val workFaq = Files.readString(work.resolve("faq-pl.txt")).linesIterator.toVector
    val workQa = workFaq.drop(workFaq.indexWhere(_.contains("Czy pracodawca jest stroną")).ensuring(_ >= 0))
      .takeWhile(l => !l.contains("ciasteczek"))
    Books.write(work.resolve("study"),
      (Books.between(work.resolve("info-pl.txt"), "Wprowadzenie", "Dokumenty") ++
        Vector("", "PYTANIA I ODPOWIEDZI", "") ++ workQa).mkString("\n"),
      { val r = Random(20261122); (1 to 160).toVector.map(Work.sampled(_, r)) })
