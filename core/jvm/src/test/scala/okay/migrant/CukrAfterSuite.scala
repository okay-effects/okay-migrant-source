package okay.migrant

import okay.study.Verdict

/** the reference reading of what follows the application answers the office's own questions as the office does */
class CukrAfterSuite extends munit.FunSuite:
  for (what, e) <- CukrAfter.official do
    test(s"official question $what"):
      val verdict = Verdict.of(CukrAfter.reference, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")

/** the model a teacher wrote from the official answers (models/pl-cukr-after.json) against the office's own
 * questions and against the reference over any number */
class CukrAfterTeacherSuite extends munit.FunSuite:
  private val model = okay.study.Model.parse(java.nio.file.Files.readString(
    java.nio.file.Path.of("models/pl-cukr-after.json"))).fold(e => fail(e), identity)

  for (what, e) <- CukrAfter.official do
    test(s"the teacher's model, official question $what"):
      val verdict = Verdict.of(model, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")

  test("the teacher's model and the reference agree on three thousand cases, any number from 0 to 400"):
    val rnd = scala.util.Random(5)
    val Number = """\d+""".r
    val differ = (1 to 3000).flatMap { k =>
      val sentences = CukrAfter.sampled(k, rnd).sentences.map(s => Number.replaceFirstIn(s, rnd.nextInt(401).toString))
      def said(m: okay.study.Model) = okay.study.Solve.run(m, sentences).map(_.map(_._2).toSet)
      Option.when(said(CukrAfter.reference) != said(model))(
        s"${sentences.mkString("; ")} → reference ${said(CukrAfter.reference)}, teacher ${said(model)}")
    }
    assertEquals(differ.take(5).toVector, Vector.empty[String], s"${differ.size} differ")
