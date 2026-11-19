//> using scala 3.3.8

import java.nio.file.{Files, Path}

/** LINES AT MOST 120 CHARACTERS: a line too long because of a string literal on it is broken inside that literal
 * at spaces — the literal becomes `("… " +` and its rest on the next lines, two further in, closed by `)` (the
 * parentheses keep `a -> "…" + "…"` what it was); with no literal to break, after the last comma outside the
 * literals. A line inside a triple-quoted text, or with neither, is left as it is and named. `scala-cli run scripts/wrap.scala -- <file>…` */
@main def wrap(files: String*): Unit =
  def width(s: String) = s.codePointCount(0, s.length)
  for file <- files if file.endsWith(".scala") do
    val path = Path.of(file)
    var triple = false
    val out = Vector.newBuilder[String]
    def broken(line: String): Vector[String] =
      if width(line) <= 120 then Vector(line)
      else
        // the string literals of the line: (the opening quote, the closing quote); the longest is broken
        val spans = Vector.newBuilder[(Int, Int)]
        var i = 0
        while i < line.length do
          if line(i) == '"' then
            var j = i + 1
            while j < line.length && line(j) != '"' do j += (if line(j) == '\\' then 2 else 1)
            spans += (i -> j); i = j + 1
          else i += 1
        val indent = line.takeWhile(_ == ' ') + "  "
        spans.result().filter((a, b) => b < line.length).maxByOption((a, b) => b - a) match
          case Some((a, b)) if line.substring(a + 1, b).contains(' ') =>
            val words = line.substring(a + 1, b).split("(?<= )").toVector
            // the first piece fills its line; the others theirs
            val pieces = words.foldLeft(Vector.empty[String]) { (acc, w) =>
              val room = if acc.size <= 1 then 116 - (if acc.isEmpty then width(line.take(a)) + 2 else indent.length + 1)
                else 116 - indent.length - 1
              acc.lastOption match
                case Some(last) if width(last) + width(w) <= room => acc.init :+ (last + w)
                case _ => acc :+ w
            }
            val head = line.take(a) + "(\"" + pieces.head + "\"" + (if pieces.size > 1 then " +" else "")
            val middle = pieces.drop(1).dropRight(1).map(p => indent + "\"" + p + "\" +")
            val last = if pieces.size > 1 then Vector(indent + "\"" + pieces.last + "\")" + line.drop(b + 1))
              else Vector.empty
            if pieces.size == 1 then afterComma(line, spans.result(), indent)
            else (head +: middle) ++ last.flatMap(l => if width(l) > 120 then broken(l) else Vector(l))
          case _ => afterComma(line, spans.result(), indent)
    /** no literal to break: the line broken after the last comma that is outside every literal */
    def afterComma(line: String, spans: Vector[(Int, Int)], indent: String): Vector[String] =
      (0 until math.min(line.length, 119)).filter(k => line(k) == ',' && !spans.exists((a, b) => a < k && k < b))
        .lastOption.filter(_ > indent.length) match
        case Some(k) => line.take(k + 1) +: broken(indent + line.drop(k + 1).trim)
        case None => println(s"$file: left long — ${line.trim.take(60)}"); Vector(line)
    for line <- Files.readString(path).linesIterator do
      val marks = "\"\"\"".r.findAllIn(line).size
      if triple || marks % 2 == 1 then
        if width(line) > 120 then println(s"$file: in a text block — ${line.trim.take(60)}")
        out += line
      else out ++= broken(line)
      if marks % 2 == 1 then triple = !triple
    Files.writeString(path, out.result().mkString("\n") + "\n")
