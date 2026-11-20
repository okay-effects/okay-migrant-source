package okay.migrant

import okay.study.{Exercise, Model, Solve}

import scala.util.Random

/** THE SINGLE PERMIT — TEMPORARY RESIDENCE AND WORK (Poland; `books/pl/work/`, the Office for Foreigners' MOS
 * portal, fetched 2026-11-20). A case: where the person is and on what basis, the purpose (work of more than three
 * months that is not seasonal, not a posting and not one's own business), the gross monthly pay (all employers
 * together; the minimum is 4806 zł in 2026, whatever the hours), pay against comparable workers, insurance, the
 * starost's list of closed occupations and its exceptions. The answer: `eligible`, or every reason not. The
 * reference is MY reading of the information page and the official answers; `official` are the office's
 * statements, as cases, with what follows */
object Work:
  val reference: Model = Model.parse(
    """{"sentences": [
      |  {"pattern": "the applicant is in Poland legally on a basis that allows applying", "facts": ["basis(allowed)"]},
      |  {"pattern": "the applicant is in Poland on an excluded basis", "facts": ["basis(excluded)"]},
      |  {"pattern": "the applicant is outside Poland", "facts": ["basis(abroad)"]},
      |  {"pattern": "the purpose is work of more than 3 months, not seasonal, not a posting", "facts": ["purpose(work)"]},
      |  {"pattern": "the purpose is seasonal work", "facts": ["purpose(seasonal)"]},
      |  {"pattern": "the purpose is a posting by a foreign employer", "facts": ["purpose(posted)"]},
      |  {"pattern": "the purpose is running one's own business", "facts": ["purpose(business)"]},
      |  {"pattern": "the purpose is work of 3 months or less", "facts": ["purpose(short)"]},
      |  {"pattern": "the gross monthly pay from all employers is {N:[0-9]+} zl", "facts": ["pay({N})"]},
      |  {"pattern": "the pay is not lower than that of comparable workers", "facts": ["comparable(yes)"]},
      |  {"pattern": "the pay is lower than that of comparable workers", "facts": ["comparable(no)"]},
      |  {"pattern": "the applicant has health insurance", "facts": ["insured(yes)"]},
      |  {"pattern": "the applicant has no health insurance", "facts": ["insured(no)"]},
      |  {"pattern": "the occupation is not on the starost's list", "facts": ["listed(no)"]},
      |  {"pattern": "the occupation is on the starost's list", "facts": ["listed(yes)"]},
      |  {"pattern": "the occupation is on the starost's list but the applicant continues the same job with the same employer or is a citizen of Turkey", "facts": ["listed(excepted)"]}],
      | "terms": [],
      | "rules": ["refused(apply_in_poland) :- basis(abroad).",
      |           "refused(basis_excluded) :- basis(excluded).",
      |           "refused(another_permit_seasonal) :- purpose(seasonal).",
      |           "refused(another_permit_posted) :- purpose(posted).",
      |           "refused(another_permit_business) :- purpose(business).",
      |           "refused(stay_not_over_3_months) :- purpose(short).",
      |           "refused(pay_under_minimum) :- pay(N), N < 4806.",
      |           "refused(pay_under_comparable) :- comparable(no).",
      |           "refused(no_insurance) :- insured(no).",
      |           "refused(occupation_closed) :- listed(yes).",
      |           "blocked(yes) :- refused(R).",
      |           "verdict(R) :- refused(R).",
      |           "verdict(eligible) :- purpose(work), not blocked(yes)."],
      | "answers": [{"pattern": "{V:[a-z0-9_]+}", "goal": "verdict(V)", "unordered": false}]}""".stripMargin)
    .fold(sys.error, identity)

  val allowed = "the applicant is in Poland legally on a basis that allows applying"
  val excluded = "the applicant is in Poland on an excluded basis"
  val abroad = "the applicant is outside Poland"
  val work = "the purpose is work of more than 3 months, not seasonal, not a posting"
  val seasonal = "the purpose is seasonal work"
  val posted = "the purpose is a posting by a foreign employer"
  val business = "the purpose is running one's own business"
  val short = "the purpose is work of 3 months or less"
  def pay(zl: Int) = s"the gross monthly pay from all employers is $zl zl"
  def comparable(yes: Boolean) =
    if yes then "the pay is not lower than that of comparable workers" else "the pay is lower than that of comparable workers"
  def insured(yes: Boolean) = if yes then "the applicant has health insurance" else "the applicant has no health insurance"
  val notListed = "the occupation is not on the starost's list"
  val listed = "the occupation is on the starost's list"
  val excepted = "the occupation is on the starost's list but the applicant continues the same job with the same " +
    "employer or is a citizen of Turkey"

  private def answer(sentences: Vector[String]): String =
    Solve.run(reference, sentences).fold(sys.error, _.map(_._2).sorted.mkString(" + "))

  private val good = Vector(allowed, work, pay(5200), comparable(true), insured(true), notListed)

  /** the office's own statements, as I state them as cases, and what follows */
  val official: Vector[(String, Exercise)] = Vector(
    "work of more than three months, the minimum pay, insured, an open occupation" -> Exercise(1, good, "eligible"),
    "part-time: at least 4806 zł gross a month whatever the hours — 4806 is enough" ->
      Exercise(2, good.updated(2, pay(4806)), "eligible"),
    "… less is not" -> Exercise(3, good.updated(2, pay(3000)), "pay_under_minimum"),
    "several employers: the pay counted together (1/4 and 3/4 of a post over 4806 in all)" ->
      Exercise(4, good.updated(2, pay(4900)), "eligible"),
    "pay not lower than that of comparable workers" ->
      Exercise(5, good.updated(3, comparable(false)), "pay_under_comparable"),
    "an occupation on the starost's list" -> Exercise(6, good.updated(5, listed), "occupation_closed"),
    "… but the same job with the same employer continues" -> Exercise(7, good.updated(5, excepted), "eligible"),
    "seasonal workers are excluded: another permit" ->
      Exercise(8, good.updated(1, seasonal), "another_permit_seasonal"),
    "workers posted by a foreign employer are excluded" -> Exercise(9, good.updated(1, posted), "another_permit_posted"),
    "running a business: another permit" -> Exercise(10, good.updated(1, business), "another_permit_business"),
    "a tourist or a family-visit visa: excluded" -> Exercise(11, good.updated(0, excluded), "basis_excluded"),
    "an application from abroad is not admissible" -> Exercise(12, good.updated(0, abroad), "apply_in_poland"),
    "the purpose must justify a stay of more than three months" ->
      Exercise(13, good.updated(1, short), "stay_not_over_3_months"))

  /** a case sampled: each fact on both sides of its rule, most of them good */
  def sampled(number: Int, rnd: Random): Exercise =
    def one[A](xs: A*): A = xs(rnd.nextInt(xs.size))
    def mostly: Boolean = rnd.nextInt(5) > 0
    val sentences = Vector(if mostly then allowed else one(excluded, abroad),
      if mostly then work else one(seasonal, posted, business, short), pay(one(2500, 4000, 4805, 4806, 4807, 6000, 9000)),
      comparable(mostly), insured(mostly), if mostly then notListed else one(listed, excepted))
    Exercise(number, rnd.shuffle(sentences), answer(sentences))
