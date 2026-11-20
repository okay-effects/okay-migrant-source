package okay.migrant

import okay.study.{Exercise, Model, Solve}

import scala.util.Random

/** PESEL AND PESEL WITH THE UKR STATUS (Poland; `books/pl/pesel/`: MSWiA's questions and answers and the City of
 * Warsaw's pages, fetched 2026-11-20). Three situations, one model: getting PESEL UKR (who, the 30 days from the
 * border, the passport, the exclusions), keeping the UKR status (more than 30 days away, another permit or
 * protection, an application for international protection, protection in another EU state, an EU citizenship,
 * no passport shown by 31 August 2026), and an ordinary PESEL (by registering one's residence, or on application
 * when a law requires the number). The reference is MY reading; `official` are the offices' statements, as cases */
object Pesel:
  val reference: Model = Model.parse(
    """{"sentences": [
      |  {"pattern": "the applicant is a Ukrainian citizen who lived in Ukraine before 24 February 2022, or a family member", "facts": ["who(ukrainian)"]},
      |  {"pattern": "the applicant is a foreigner who had protection in Ukraine before 24 February 2022, or a family member", "facts": ["who(protected)"]},
      |  {"pattern": "the applicant is a foreigner who stayed legally in Ukraine before 24 February 2022 and cannot return home safely", "facts": ["who(resident)"]},
      |  {"pattern": "the applicant is none of these", "facts": ["who(none)"]},
      |  {"pattern": "the applicant crossed the Polish border {N:[0-9]+} days ago", "facts": ["crossed({N})"]},
      |  {"pattern": "the applicant has a valid passport", "facts": ["passport(yes)"]},
      |  {"pattern": "the applicant has no valid passport", "facts": ["passport(no)"]},
      |  {"pattern": "the applicant holds a residence permit or a protection in Poland", "facts": ["excluded(permit)"]},
      |  {"pattern": "the applicant applied for international protection in Poland", "facts": ["excluded(application)"]},
      |  {"pattern": "the applicant has temporary protection in another EU state", "facts": ["excluded(other_state)"]},
      |  {"pattern": "the applicant entered Poland on a local border traffic permit", "facts": ["excluded(border_traffic)"]},
      |  {"pattern": "none of the exclusions applies to the applicant", "facts": ["excluded(none)"]},
      |  {"pattern": "the holder has been away from Poland for {N:[0-9]+} days at a stretch", "facts": ["away({N})"]},
      |  {"pattern": "the holder got a residence permit or a protection in Poland", "facts": ["lost(other_permit)"]},
      |  {"pattern": "the holder applied for international protection", "facts": ["lost(protection_application)"]},
      |  {"pattern": "the holder got temporary protection in another EU state", "facts": ["lost(other_eu_state)"]},
      |  {"pattern": "the holder became a Polish or another EU citizen", "facts": ["lost(citizenship)"]},
      |  {"pattern": "the holder was registered without a passport and did not show one by 31 August 2026", "facts": ["lost(no_passport)"]},
      |  {"pattern": "nothing else happened to the holder's status", "facts": ["lost(none)"]},
      |  {"pattern": "the person registers a residence in Poland", "facts": ["registers(yes)"]},
      |  {"pattern": "the person does not register a residence in Poland", "facts": ["registers(no)"]},
      |  {"pattern": "a law requires the person to have a PESEL number", "facts": ["required(yes)"]},
      |  {"pattern": "no law requires the person to have a PESEL number yet", "facts": ["required(no)"]}],
      | "terms": [],
      | "rules": ["no_ukr(not_entitled) :- who(none).",
      |           "no_ukr(too_late_30_days) :- crossed(N), N > 30.",
      |           "no_ukr(needs_passport) :- passport(no).",
      |           "no_ukr(excluded_other_permit) :- excluded(permit).",
      |           "no_ukr(excluded_protection_application) :- excluded(application).",
      |           "no_ukr(excluded_other_eu_state) :- excluded(other_state).",
      |           "no_ukr(excluded_border_traffic) :- excluded(border_traffic).",
      |           "refused(yes) :- no_ukr(R).",
      |           "verdict(R) :- no_ukr(R).",
      |           "verdict(may_get_pesel_ukr) :- crossed(N), not refused(yes).",
      |           "gone(lost_left_over_30_days) :- away(N), N > 30.",
      |           "gone(lost_other_permit) :- lost(other_permit).",
      |           "gone(lost_protection_application) :- lost(protection_application).",
      |           "gone(lost_other_eu_state) :- lost(other_eu_state).",
      |           "gone(lost_citizenship) :- lost(citizenship).",
      |           "gone(lost_no_passport) :- lost(no_passport).",
      |           "ended(yes) :- gone(R).",
      |           "verdict(R) :- gone(R).",
      |           "verdict(status_kept) :- away(N), not ended(yes).",
      |           "verdict(pesel_by_registration) :- registers(yes).",
      |           "verdict(pesel_by_application) :- registers(no), required(yes).",
      |           "verdict(no_basis_for_pesel_yet) :- registers(no), required(no)."],
      | "answers": [{"pattern": "{V:[a-z0-9_]+}", "goal": "verdict(V)", "unordered": false}]}""".stripMargin)
    .fold(sys.error, identity)

  val ukrainian = "the applicant is a Ukrainian citizen who lived in Ukraine before 24 February 2022, or a family member"
  val protectedThere = "the applicant is a foreigner who had protection in Ukraine before 24 February 2022, or a family member"
  val residentThere = "the applicant is a foreigner who stayed legally in Ukraine before 24 February 2022 and cannot " +
    "return home safely"
  val nobody = "the applicant is none of these"
  def crossed(days: Int) = s"the applicant crossed the Polish border $days days ago"
  def passport(yes: Boolean) = s"the applicant has ${if yes then "a" else "no"} valid passport"
  val holdsPermit = "the applicant holds a residence permit or a protection in Poland"
  val appliedProtection = "the applicant applied for international protection in Poland"
  val otherState = "the applicant has temporary protection in another EU state"
  val borderTraffic = "the applicant entered Poland on a local border traffic permit"
  val noExclusion = "none of the exclusions applies to the applicant"
  def away(days: Int) = s"the holder has been away from Poland for $days days at a stretch"
  val gotPermit = "the holder got a residence permit or a protection in Poland"
  val appliedLater = "the holder applied for international protection"
  val gotOtherState = "the holder got temporary protection in another EU state"
  val citizen = "the holder became a Polish or another EU citizen"
  val noPassport = "the holder was registered without a passport and did not show one by 31 August 2026"
  val nothingElse = "nothing else happened to the holder's status"
  def registers(yes: Boolean) = s"the person ${if yes then "registers" else "does not register"} a residence in Poland"
  def required(yes: Boolean) =
    if yes then "a law requires the person to have a PESEL number" else "no law requires the person to have a PESEL number yet"

  private def answer(sentences: Vector[String]): String =
    Solve.run(reference, sentences).fold(sys.error, _.map(_._2).sorted.mkString(" + "))

  private val arrived = Vector(ukrainian, crossed(10), passport(true), noExclusion)

  /** the offices' own statements, as I state them as cases, and what follows */
  val official: Vector[(String, Exercise)] = Vector(
    "MSWiA 2: a Ukrainian who has just come, within 30 days, with a passport" -> Exercise(1, arrived, "may_get_pesel_ukr"),
    "MSWiA 2: … on the thirtieth day still" -> Exercise(2, arrived.updated(1, crossed(30)), "may_get_pesel_ukr"),
    "MSWiA 2: … later, no" -> Exercise(3, arrived.updated(1, crossed(31)), "too_late_30_days"),
    "MSWiA 2: a valid passport is shown" -> Exercise(4, arrived.updated(2, passport(false)), "needs_passport"),
    "Warsaw: a foreigner with protection in Ukraine before the war" ->
      Exercise(5, arrived.updated(0, protectedThere), "may_get_pesel_ukr"),
    "Warsaw: not if one holds an EU resident permit or protection" ->
      Exercise(6, arrived.updated(3, holdsPermit), "excluded_other_permit"),
    "Warsaw: not if temporary protection was given in another EU state" ->
      Exercise(7, arrived.updated(3, otherState), "excluded_other_eu_state"),
    "Warsaw: not on a local border traffic permit" ->
      Exercise(8, arrived.updated(3, borderTraffic), "excluded_border_traffic"),
    "MSWiA 1: more than 30 days away — the status is lost" -> Exercise(9, Vector(away(31), nothingElse), "lost_left_over_30_days"),
    "MSWiA 9: 30 days away are allowed" -> Exercise(10, Vector(away(30), nothingElse), "status_kept"),
    "MSWiA 1: a temporary residence permit ends it" -> Exercise(11, Vector(away(0), gotPermit), "lost_other_permit"),
    "MSWiA 1: a Polish citizenship ends it" -> Exercise(12, Vector(away(0), citizen), "lost_citizenship"),
    "MSWiA 1: registered without a passport, not shown by 31 August 2026" ->
      Exercise(13, Vector(away(0), noPassport), "lost_no_passport"),
    "Warsaw: a foreigner who registers a residence gets a PESEL" ->
      Exercise(14, Vector(registers(true)), "pesel_by_registration"),
    "Warsaw: one who cannot register but must have a PESEL applies" ->
      Exercise(15, Vector(registers(false), required(true)), "pesel_by_application"))

  /** a case sampled: one of the three situations, each fact on both sides of its rule */
  def sampled(number: Int, rnd: Random): Exercise =
    def one[A](xs: A*): A = xs(rnd.nextInt(xs.size))
    def mostly: Boolean = rnd.nextInt(4) > 0
    val sentences = rnd.nextInt(3) match
      case 0 => Vector(if mostly then one(ukrainian, protectedThere, residentThere) else nobody,
        crossed(one(1, 10, 29, 30, 31, 45, 200)), passport(mostly),
        if mostly then noExclusion else one(holdsPermit, appliedProtection, otherState, borderTraffic))
      case 1 => Vector(away(one(0, 5, 29, 30, 31, 60)),
        if mostly then nothingElse else one(gotPermit, appliedLater, gotOtherState, citizen, noPassport))
      case _ => if rnd.nextBoolean() then Vector(registers(true)) else Vector(registers(false), required(rnd.nextBoolean()))
    Exercise(number, rnd.shuffle(sentences), answer(sentences))
