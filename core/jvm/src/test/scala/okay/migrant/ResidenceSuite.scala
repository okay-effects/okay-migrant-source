package okay.migrant

import okay.study.Verdict

/** the reference readings of the two residence permits answer the office's own statements as the office does */
class ResidenceSuite extends munit.FunSuite:
  for (what, e) <- Permanent.official do
    test(s"permanent residence: $what"):
      val verdict = Verdict.of(Permanent.reference, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")
  for (what, e) <- Arrival.official do
    test(s"arriving: $what"):
      val verdict = Verdict.of(Arrival.reference, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")
  for (what, e) <- Pesel.official do
    test(s"PESEL: $what"):
      val verdict = Verdict.of(Pesel.reference, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")
  for (what, e) <- Work.official do
    test(s"the single permit: $what"):
      val verdict = Verdict.of(Work.reference, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")
  for (what, e) <- EuResident.official do
    test(s"long-term EU resident: $what"):
      val verdict = Verdict.of(EuResident.reference, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")

/** the models teachers wrote from the office's pages, against the office's statements and against the references
 * on thousands of sampled cases */
class ResidenceTeacherSuite extends munit.FunSuite:
  private def model(file: String) = okay.study.Model.parse(java.nio.file.Files.readString(
    java.nio.file.Path.of("models", file))).fold(e => fail(e), identity)
  private val permanent = model("pl-permanent.json")
  private val eu = model("pl-eu-resident.json")
  private val work = model("pl-work.json")
  private val pesel = model("pl-pesel.json")
  private val arrival = model("pl-arrival.json")

  for (what, e) <- Arrival.official do
    test(s"the teacher's arrival model: $what"):
      val verdict = Verdict.of(arrival, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")

  for (what, e) <- Arrival.official do
    test(s"arriving: $what"):
      val verdict = Verdict.of(Arrival.reference, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")
  for (what, e) <- Pesel.official do
    test(s"the teacher's PESEL model: $what"):
      val verdict = Verdict.of(pesel, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")

  for (what, e) <- Permanent.official do
    test(s"the teacher's permanent residence model: $what"):
      val verdict = Verdict.of(permanent, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")
  for (what, e) <- Work.official do
    test(s"the teacher's single permit model: $what"):
      val verdict = Verdict.of(work, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")
  for (what, e) <- EuResident.official do
    test(s"the teacher's EU resident model: $what"):
      val verdict = Verdict.of(eu, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")

  private type Sampler = (Int, scala.util.Random) => okay.study.Exercise

  private def agree(reference: okay.study.Model, taught: okay.study.Model, sample: Sampler, seed: Int): Unit =
    val rnd = scala.util.Random(seed)
    val Number = """\d+""".r
    val differ = (1 to 3000).flatMap { k =>
      // any number from 0 to 150, not only the thresholds' sides
      val sentences = sample(k, rnd).sentences.map(s => Number.replaceFirstIn(s, rnd.nextInt(151).toString))
      def said(m: okay.study.Model) = okay.study.Solve.run(m, sentences).map(_.map(_._2).toSet)
      // a number the law fixes (16, 2 years) is part of a sentence: a case the reference cannot read is no case
      Option.when(said(reference).isRight && said(reference) != said(taught))(
        s"${sentences.mkString("; ")} → reference ${said(reference)}, teacher ${said(taught)}")
    }
    assertEquals(differ.take(5).toVector, Vector.empty[String], s"${differ.size} differ")

  test("permanent residence: the teacher's model and the reference agree on three thousand cases"):
    agree(Permanent.reference, permanent, Permanent.sampled, 7)
  test("EU resident: the teacher's model and the reference agree on three thousand cases"):
    agree(EuResident.reference, eu, EuResident.sampled, 8)
  test("the single permit: the teacher's model and the reference agree on three thousand cases"):
    agree(Work.reference, work, Work.sampled, 9)
  test("PESEL: the teacher's model and the reference agree on three thousand cases"):
    agree(Pesel.reference, pesel, Pesel.sampled, 10)
  test("arriving: the teacher's model and the reference agree on three thousand cases"):
    agree(Arrival.reference, arrival, Arrival.sampled, 11)
