//> using scala 3.3.8

import java.nio.file.{Files, Path}

/** LINES AT MOST 120 CHARACTERS: a line too long because of a string literal on it is broken inside that literal
 * at a space (`"… " +` and the rest on the next line, two further in). A line inside a triple-quoted text is
 * left as it is and named. `scala-cli run scripts/wrap.scala -- <file>…` */
@main def wrap(files: String*): Unit =
  for file <- files do
    val path = Path.of(file)
    var triple = false
    val out = Vector.newBuilder[String]
    def broken(line: String): Vector[String] =
      if line.codePointCount(0, line.length) <= 120 then Vector(line)
      else
        // the string literals of the line: (the opening quote, the closing quote)
        val spans = Vector.newBuilder[(Int, Int)]
        var i = 0
        while i < line.length do
          if line(i) == '"' then
            var j = i + 1
            while j < line.length && line(j) != '"' do j += (if line(j) == '\\' then 2 else 1)
            spans += (i -> j); i = j + 1
          else i += 1
        val indent = line.takeWhile(_ == ' ')
        // the cut: the last space inside a literal that leaves room for `" +`
        val cut = spans.result().flatMap((a, b) => (a + 2 until math.min(b, 116)).filter(k => line(k) == ' '))
          .maxOption
        cut match
          case None => println(s"$file: left long — ${line.trim.take(60)}"); Vector(line)
          case Some(k) => (line.take(k + 1) + "\" +") +: broken(indent + "  \"" + line.drop(k + 1))
    for line <- Files.readString(path).linesIterator do
      val marks = "\"\"\"".r.findAllIn(line).size
      if triple || marks % 2 == 1 then
        if line.codePointCount(0, line.length) > 120 then println(s"$file: in a text block — ${line.trim.take(60)}")
        out += line
      else out ++= broken(line)
      if marks % 2 == 1 then triple = !triple
    Files.writeString(path, out.result().mkString("\n") + "\n")
