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

    // PESEL: MSWiA's answers 1, 2 and 9, the City of Warsaw's two pages (their main text), as published
    val pesel = Path.of("books/pl/pesel")
    val mswia = Files.readString(pesel.resolve("mswia-faq-pl.txt")).linesIterator.toVector
    def warsaw(file: String) = Files.readString(pesel.resolve(file)).linesIterator.toVector
      .dropWhile(l => !l.contains("Zaktualizowano")).takeWhile(l => !l.contains("Nie znalazłeś informacji"))
      .filter(_.trim.length > 20)
    val mswiaPart = mswia.takeWhile(l => !l.trim.startsWith("3. Co z przechodzeniem")) ++
      mswia.dropWhile(l => !l.trim.startsWith("9. Czy będzie można")).takeWhile(l => !l.trim.startsWith("10. "))
    Books.write(pesel.resolve("study"),
      (mswiaPart ++ Vector("", "URZĄD M.ST. WARSZAWY", "") ++ warsaw("warszawa-ukr-pl.txt") ++ Vector("") ++
        warsaw("warszawa-pesel-pl.txt")).mkString("\n"),
      { val r = Random(20261123); (1 to 160).toVector.map(Pesel.sampled(_, r)) })

    // ARRIVING: the pages' main text, as published (their menus, cookies and licence left out), and the parts of the
    // books already here that the steps rest on
    val arrival = Path.of("books/pl/arrival")
    def text(dir: Path, file: String) = Files.readString(dir.resolve(file)).linesIterator.toVector
      .filter(_.trim.length > 40).filterNot(l => Vector("ciasteczek", "Creative Commons", "FAMI", "preferencji",
        "sesji dostępu", "komunikatów", "statystycznych", "blokowanie", "Zezwolenie na pobyt rezydenta").exists(l.contains))
    def from(lines: Vector[String], start: String, count: Int) =
      lines.drop(lines.indexWhere(_.contains(start)).ensuring(_ >= 0, start)).take(count)
    val purposes = Files.readString(arrival.resolve("purposes-pl.txt")).linesIterator.map(_.trim).toVector
    val lesson = Vector("RUCH BEZWIZOWY") ++ from(text(arrival, "visa-free-pl.txt"), "Wjazd do Polski w ruchu", 4) ++
      from(text(arrival, "visa-free-pl.txt"), "W przypadku cudzoziemców podróżujących w ruchu", 1) ++
      Vector("", "WIZY") ++ from(text(arrival, "visas-pl.txt"), "Wizy Schengen są wizami", 1) ++
      from(text(arrival, "visas-pl.txt"), "Typ wizy, o którą powinien", 1) ++
      Vector("", "OBYWATEL UE") ++ from(text(arrival, "eu-citizen-pl.txt"), "Posiadając obywatelstwo UE", 22) ++
      Vector("", "WYKONYWANIE PRACY") ++ from(text(arrival, "work-rules-pl.txt"), "O ile prawo Unii zapewnia", 5) ++
      from(text(arrival, "work-rules-pl.txt"), "Swobodny dostęp do polskiego rynku pracy dotyczy", 30) ++
      Vector("", "ZEZWOLENIE NA POBYT CZASOWY I PRACĘ") ++
      from(Files.readString(Path.of("books/pl/work/info-pl.txt")).linesIterator.toVector,
        "Cudzoziemcy, którzy są wyłączeni z możliwości uzyskania tego zezwolenia", 22) ++
      from(text(Path.of("books/pl/work"), "info-pl.txt"), "Jeżeli wniosek został złożony prawidłowo", 1) ++
      Vector("", "STUDIA") ++ from(text(arrival, "studies-pl.txt"), "Zezwolenie na pobyt czasowy w celu kształcenia się na studiach uprawnia", 8) ++
      Vector("", "CELE POBYTU CZASOWEGO") ++ from(purposes, "Wybierz główny cel pobytu", 1) ++
      purposes.drop(purposes.indexOf("Wykonywanie pracy")).take(22).distinct ++
      Vector("", "PESEL") ++ warsaw("warszawa-pesel-pl.txt").take(6) ++
      Vector("", "OBYWATELE UKRAINY (MSWiA, UdSC)") ++ mswiaPart.take(40) ++
      from(text(Path.of("books/pl/cukr"), "faq-pl.txt"), "Status ten umożliwia", 1) ++
      from(text(Path.of("books/pl/cukr"), "info-pl.txt"), "posiadałeś status UKR nieprzerwanie", 1)
    Books.write(arrival.resolve("study"), lesson.mkString("\n"),
      { val r = Random(20261124); (1 to 160).toVector.map(Arrival.sampled(_, r)) })
