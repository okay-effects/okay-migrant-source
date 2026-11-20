package okay.migrant

import okay.study.Verdict

/** the reference readings of the two residence permits answer the office's own statements as the office does */
class ResidenceSuite extends munit.FunSuite:
  for (what, e) <- Permanent.official do
    test(s"permanent residence: $what"):
      val verdict = Verdict.of(Permanent.reference, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")
  for (what, e) <- EuResident.official do
    test(s"long-term EU resident: $what"):
      val verdict = Verdict.of(EuResident.reference, e)
      assert(verdict.isInstanceOf[Verdict.Right_], s"$verdict for ${e.sentences.mkString("; ")}")
