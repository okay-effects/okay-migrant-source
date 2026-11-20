package okay.migrant

import okay.study.{Exercise, Model, Solve}

import scala.util.Random

/** THE LONG-TERM EU RESIDENT PERMIT (Poland; `books/pl/eu-resident/`, the Office for Foreigners' MOS portal,
 * fetched 2026-11-20). A case: the basis of the stay at the time of applying, the counted legal stay (the person
 * counts it by the office's rule, which the question says: a student's stay half, temporary protection, school
 * and others not at all), the longest absence and all absences together (an absence for a Polish employer, a
 * special personal situation of up to six months or a Polish university's practice does not count), income,
 * insurance, Polish. The answer: `eligible`, or every reason not. The reference is MY reading of the information
 * page and the official answers; `official` are the office's own statements, as cases, with what follows */
object EuResident:
  val reference: Model = Model.parse(
    """{"sentences": [
      |  {"pattern": "the stay at the time of applying is on an excluded basis", "facts": ["basis(excluded)"]},
      |  {"pattern": "the stay at the time of applying is on a basis that is not excluded", "facts": ["basis(allowed)"]},
      |  {"pattern": "the counted legal stay in Poland is {N:[0-9]+} months", "facts": ["counted({N})"]},
      |  {"pattern": "the longest absence from Poland lasted {N:[0-9]+} months", "facts": ["longest({N})"]},
      |  {"pattern": "all absences from Poland together lasted {N:[0-9]+} months", "facts": ["together({N})"]},
      |  {"pattern": "a stable and regular income above the threshold is shown", "facts": ["income(yes)"]},
      |  {"pattern": "a stable and regular income above the threshold is not shown", "facts": ["income(no)"]},
      |  {"pattern": "the applicant has health insurance", "facts": ["insured(yes)"]},
      |  {"pattern": "the applicant has no health insurance", "facts": ["insured(no)"]},
      |  {"pattern": "Polish at level B1 is confirmed by a document", "facts": ["polish(yes)"]},
      |  {"pattern": "Polish at level B1 is not confirmed by a document", "facts": ["polish(no)"]},
      |  {"pattern": "the applicant was under 16 when applying", "facts": ["polish(young)"]}],
      | "terms": [],
      | "rules": ["refused(current_basis_excluded) :- basis(excluded).",
      |           "refused(stay_too_short) :- counted(N), N < 60.",
      |           "refused(absence_too_long) :- longest(N), N > 6.",
      |           "refused(absences_too_long) :- together(N), N > 10.",
      |           "refused(income_not_shown) :- income(no).",
      |           "refused(no_insurance) :- insured(no).",
      |           "refused(no_polish_b1) :- polish(no).",
      |           "blocked(yes) :- refused(R).",
      |           "verdict(R) :- refused(R).",
      |           "verdict(eligible) :- basis(allowed), not blocked(yes)."],
      | "answers": [{"pattern": "{V:[a-z0-9_]+}", "goal": "verdict(V)", "unordered": false}]}""".stripMargin)
    .fold(sys.error, identity)

  def basis(excluded: Boolean) =
    if excluded then "the stay at the time of applying is on an excluded basis"
    else "the stay at the time of applying is on a basis that is not excluded"
  def counted(months: Int) = s"the counted legal stay in Poland is $months months"
  def longest(months: Int) = s"the longest absence from Poland lasted $months months"
  def together(months: Int) = s"all absences from Poland together lasted $months months"
  def income(shown: Boolean) =
    s"a stable and regular income above the threshold is ${if shown then "" else "not "}shown"
  def insured(yes: Boolean) = if yes then "the applicant has health insurance" else "the applicant has no health insurance"
  def polish(confirmed: Boolean) = s"Polish at level B1 is ${if confirmed then "" else "not "}confirmed by a document"
  val young = "the applicant was under 16 when applying"

  private def answer(sentences: Vector[String]): String =
    Solve.run(reference, sentences).fold(sys.error, _.map(_._2).sorted.mkString(" + "))

  private val good = Vector(basis(false), counted(60), longest(3), together(8), income(true), insured(true),
    polish(true))

  /** the office's own statements, as I state them as cases, and what follows */
  val official: Vector[(String, Exercise)] = Vector(
    "a stay of five years, legal and without a break, with income, insurance and Polish B1" ->
      Exercise(1, good, "eligible"),
    "absences: one of at most six months is allowed" ->
      Exercise(2, good.updated(2, longest(6)), "eligible"),
    "… a longer one breaks the stay" ->
      Exercise(3, good.updated(2, longest(7)), "absence_too_long"),
    "absences: all together at most ten months" ->
      Exercise(4, good.updated(3, together(10)), "eligible"),
    "… more breaks the stay" ->
      Exercise(5, good.updated(3, together(11)), "absences_too_long"),
    "a minor at school: the school years do not count toward the five" ->
      Exercise(6, good.updated(1, counted(30)).updated(6, young), "stay_too_short"),
    "under 16 when applying: no Polish certificate needed" ->
      Exercise(7, good.updated(6, young), "eligible"),
    "studying at the time of applying: the permit is excluded" ->
      Exercise(8, good.updated(0, basis(true)), "current_basis_excluded"),
    "a permanent residence holder may apply as well" -> Exercise(9, good, "eligible"),
    "Polish not confirmed by a listed document — speaking it well is not enough" ->
      Exercise(10, good.updated(6, polish(false)), "no_polish_b1"),
    "no stable and regular income over the years checked" ->
      Exercise(11, good.updated(4, income(false)), "income_not_shown"))

  /** a case sampled: each fact on both sides of its rule, most of them good */
  def sampled(number: Int, rnd: Random): Exercise =
    def one[A](xs: A*): A = xs(rnd.nextInt(xs.size))
    def mostly: Boolean = rnd.nextInt(5) > 0
    val sentences = Vector(basis(!mostly), counted(one(24, 48, 59, 60, 61, 72, 120)), longest(one(1, 3, 5, 6, 7, 9)),
      together(one(2, 6, 9, 10, 11, 14)), income(mostly), insured(mostly),
      if rnd.nextInt(6) == 0 then young else polish(mostly))
    Exercise(number, rnd.shuffle(sentences), answer(sentences))
