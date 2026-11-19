package okay.migrant

import okay.scala3.*
import okay.study.Model

import java.nio.file.{Files, Path}
import java.time.LocalDate

/** the assistant in a terminal: `sbt "core/runMain okay.migrant.Main uk"` (uk | ru | pl | en). An answer is a
 * choice's number, or a typed value where the question takes one; answers may also be given after the language
 * (`… Main ru 1 1 2`), the terminal asked for the rest */
object Main:
  private def console[R[_], Rest[_], A](language: String, ready: Iterator[String])(p: A ! R)(
      using Split[Person, R, Rest]): A ! Rest =
    handle(p, Handler.tail[Person, Rest, A, A](pure)([X] => (op: Person[X]) => op match
      case Person.Ask(q) =>
        println(s"\n${q.text.in(language)}")
        q.choices.zipWithIndex.foreach((c, i) => println(s"  ${i + 1}. ${c.label.in(language)}"))
        print("> ")
        val line =
          if ready.hasNext then { val a = ready.next(); println(a); a }
          else Option(scala.io.StdIn.readLine()).getOrElse(sys.exit(0)).trim
        line.toIntOption.flatMap(n => q.choices.lift(n - 1)) match
          case Some(c) => Person.Said.Chose(c.id)
          case None => Person.Said.Typed(line)))

  def main(args: Array[String]): Unit =
    val language = args.headOption.getOrElse("uk")
    val model = Model.parse(Files.readString(Path.of("models/pl-cukr.json"))).fold(sys.error, identity)
    val now = LocalDate.now
    val topic = CukrTopic.topic(model, now.getYear * 10000 + now.getMonthValue * 100 + now.getDayOfMonth)
    println(topic.name.in(language))
    val outcome = run(console(language, args.drop(1).iterator)(Interview.run[Person](topic)))
    println()
    println(outcome.said(topic, language))
