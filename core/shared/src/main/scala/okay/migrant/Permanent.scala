package okay.migrant

import okay.study.{Exercise, Model, Solve}

import scala.util.Random

/** THE PERMANENT RESIDENCE PERMIT (Poland; `books/pl/permanent/`, the Office for Foreigners' MOS portal, fetched
 * 2026-11-20). A case: the ground the person relies on (one of the law's list) and the facts that ground asks
 * for. The answer: `eligible`, or why not. The reference is MY reading of the information page and the official
 * answers; `official` are the office's own answers, as cases, with what they say */
object Permanent:
  val reference: Model = Model.parse(
    """{"sentences": [
      |  {"pattern": "the ground is being a child of a Polish citizen in their parental custody", "facts": ["ground(polish_child)"]},
      |  {"pattern": "the ground is a valid Karta Polaka and the will to settle in Poland", "facts": ["ground(karta_polaka)"]},
      |  {"pattern": "the ground is Polish origin and the will to settle in Poland", "facts": ["ground(polish_origin)"]},
      |  {"pattern": "the ground is marriage to a Polish citizen", "facts": ["ground(spouse)"]},
      |  {"pattern": "the ground is refugee status, subsidiary protection or humanitarian stay", "facts": ["ground(protection)"]},
      |  {"pattern": "the ground is a tolerated stay", "facts": ["ground(tolerated)"]},
      |  {"pattern": "the ground is asylum granted in Poland", "facts": ["ground(asylum)"]},
      |  {"pattern": "the ground is being a child of a foreigner settled in Poland", "facts": ["ground(settled_child)"]},
      |  {"pattern": "the ground is work in an occupation desired for the Polish economy", "facts": ["ground(desired_occupation)"]},
      |  {"pattern": "none of the law's grounds applies", "facts": ["ground(none)"]},
      |  {"pattern": "the marriage has lasted {N:[0-9]+} years", "facts": ["married({N})"]},
      |  {"pattern": "right before applying the applicant stayed in Poland without a break for at least 2 years on a temporary permit for this marriage", "facts": ["two_years(yes)"]},
      |  {"pattern": "right before applying the applicant did not stay in Poland for 2 years without a break on a temporary permit for this marriage", "facts": ["two_years(no)"]},
      |  {"pattern": "the stay in Poland on this ground has lasted {N:[0-9]+} years without a break", "facts": ["years({N})"]},
      |  {"pattern": "the child was born after the parent's permit was granted or while the parent was in Poland on a temporary permit or protection", "facts": ["born_in_time(yes)"]},
      |  {"pattern": "the child was born at another time", "facts": ["born_in_time(no)"]}],
      | "terms": [],
      | "rules": ["verdict(eligible) :- ground(polish_child).",
      |           "verdict(eligible) :- ground(karta_polaka).",
      |           "verdict(eligible) :- ground(polish_origin).",
      |           "verdict(eligible) :- ground(asylum).",
      |           "short(married_under_3_years) :- ground(spouse), married(N), N < 3.",
      |           "short(no_2_years_on_marriage_permit) :- ground(spouse), two_years(no).",
      |           "spouse_short(yes) :- short(R).",
      |           "verdict(R) :- short(R).",
      |           "verdict(eligible) :- ground(spouse), married(N), two_years(yes), not spouse_short(yes).",
      |           "verdict(protection_under_5_years) :- ground(protection), years(N), N < 5.",
      |           "verdict(eligible) :- ground(protection), years(N), N >= 5.",
      |           "verdict(tolerated_under_10_years) :- ground(tolerated), years(N), N < 10.",
      |           "verdict(eligible) :- ground(tolerated), years(N), N >= 10.",
      |           "verdict(child_born_at_another_time) :- ground(settled_child), born_in_time(no).",
      |           "verdict(eligible) :- ground(settled_child), born_in_time(yes).",
      |           "verdict(desired_occupation_not_possible_yet) :- ground(desired_occupation).",
      |           "verdict(no_ground) :- ground(none)."],
      | "answers": [{"pattern": "{V:[a-z0-9_]+}", "goal": "verdict(V)", "unordered": false}]}""".stripMargin)
    .fold(sys.error, identity)

  val polishChild = "the ground is being a child of a Polish citizen in their parental custody"
  val kartaPolaka = "the ground is a valid Karta Polaka and the will to settle in Poland"
  val polishOrigin = "the ground is Polish origin and the will to settle in Poland"
  val spouse = "the ground is marriage to a Polish citizen"
  val protection = "the ground is refugee status, subsidiary protection or humanitarian stay"
  val tolerated = "the ground is a tolerated stay"
  val asylum = "the ground is asylum granted in Poland"
  val settledChild = "the ground is being a child of a foreigner settled in Poland"
  val desired = "the ground is work in an occupation desired for the Polish economy"
  val none = "none of the law's grounds applies"
  def married(years: Int) = s"the marriage has lasted $years years"
  def twoYears(yes: Boolean) =
    if yes then "right before applying the applicant stayed in Poland without a break for at least 2 years on a " +
      "temporary permit for this marriage"
    else "right before applying the applicant did not stay in Poland for 2 years without a break on a temporary " +
      "permit for this marriage"
  def years(n: Int) = s"the stay in Poland on this ground has lasted $n years without a break"
  def bornInTime(yes: Boolean) =
    if yes then "the child was born after the parent's permit was granted or while the parent was in Poland on a " +
      "temporary permit or protection"
    else "the child was born at another time"

  private def answer(sentences: Vector[String]): String =
    Solve.run(reference, sentences).fold(sys.error, _.map(_._2).sorted.mkString(" + "))

  /** the office's own answers, as I state them as cases, and what they say */
  val official: Vector[(String, Exercise)] = Vector(
    "a holder of the Karta Polaka needs no earlier stay in Poland" -> Exercise(1, Vector(kartaPolaka), "eligible"),
    "a refugee: five years without a break" -> Exercise(2, Vector(protection, years(5)), "eligible"),
    "… four are not enough" -> Exercise(3, Vector(protection, years(4)), "protection_under_5_years"),
    "a spouse of a Polish citizen right after the wedding: no" ->
      Exercise(4, Vector(spouse, married(0), twoYears(false)),
        "married_under_3_years + no_2_years_on_marriage_permit"),
    "a spouse: three years of marriage and two years on a temporary permit for it" ->
      Exercise(5, Vector(spouse, married(3), twoYears(true)), "eligible"),
    "a person of Polish origin who will settle" -> Exercise(6, Vector(polishOrigin), "eligible"),
    "a child born in Poland while the foreign parent held a temporary permit" ->
      Exercise(7, Vector(settledChild, bornInTime(true)), "eligible"),
    "not every child born in Poland: born at another time" ->
      Exercise(8, Vector(settledChild, bornInTime(false)), "child_born_at_another_time"),
    "a child of a Polish citizen in their parental custody" -> Exercise(9, Vector(polishChild), "eligible"),
    "a tolerated stay: ten years" -> Exercise(10, Vector(tolerated, years(10)), "eligible"),
    "the desired occupation: not possible yet, the regulation is not issued" ->
      Exercise(11, Vector(desired), "desired_occupation_not_possible_yet"))

  /** a case sampled: a ground and what it asks for, each number on both sides of its rule */
  def sampled(number: Int, rnd: Random): Exercise =
    def one[A](xs: A*): A = xs(rnd.nextInt(xs.size))
    val sentences = rnd.nextInt(10) match
      case 0 => Vector(one(polishChild, kartaPolaka, polishOrigin, asylum))
      case 1 | 2 | 3 => Vector(spouse, married(one(0, 1, 2, 3, 4, 7)), twoYears(rnd.nextInt(3) > 0))
      case 4 | 5 => Vector(protection, years(one(1, 3, 4, 5, 6, 9)))
      case 6 => Vector(tolerated, years(one(4, 8, 9, 10, 11, 15)))
      case 7 => Vector(settledChild, bornInTime(rnd.nextBoolean()))
      case 8 => Vector(desired)
      case _ => Vector(none)
    Exercise(number, rnd.shuffle(sentences), answer(sentences))
