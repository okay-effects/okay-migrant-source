package okay.migrant

import okay.study.{Exercise, Model, Solve}

import scala.util.Random

/** THE SECOND BOOK OF THE CUKR CARD: what follows — after the application, when the card is ready, while it is
 * held, after a refusal, a lost card, work (the official answers 2–5, 9, 43–60 in `books/pl/cukr/`). A case is
 * one or two canonical sentences of a situation; the answer is what follows from it. The reference is MY reading;
 * `official` are the office's own questions, stated by hand, with what the office answers.
 * One place the office's texts disagree: an absence of exactly six months — "powyżej 6 miesięcy" (the sheet;
 * answer 9), "co najmniej 6 miesięcy" (answer 54), "jednorazowo na okres 6 miesięcy" (answer 53). The model does
 * not choose: at exactly six it says the texts differ */
object CukrAfter:
  val reference: Model = Model.parse(
    """{"sentences": [
      |  {"pattern": "the application is made and the applicant leaves Poland for {N:[0-9]+} days", "facts": ["leaves({N})"]},
      |  {"pattern": "the card is ready and {N:[0-9]+} days have passed since the office said so", "facts": ["waits({N})"]},
      |  {"pattern": "the card is held and the holder is away from Poland for {N:[0-9]+} months at a stretch", "facts": ["away({N})"]},
      |  {"pattern": "the card is held and the holder moved {N:[0-9]+} working days ago", "facts": ["moved({N})"]},
      |  {"pattern": "the card was refused and the decision was delivered {N:[0-9]+} days ago", "facts": ["refused({N})"]},
      |  {"pattern": "the refusal was for a fee not paid", "facts": ["unpaid(yes)"]},
      |  {"pattern": "the refusal was for another reason", "facts": ["unpaid(no)"]},
      |  {"pattern": "the card is held and was lost for the first time", "facts": ["lost(first)"]},
      |  {"pattern": "the card is held and was lost again by the holder's fault", "facts": ["lost(again)"]},
      |  {"pattern": "the card is held and the holder wants to work", "facts": ["wants(work)"]},
      |  {"pattern": "the card is held and the holder wants to run a business", "facts": ["wants(business)"]}],
      | "terms": [],
      | "rules": ["verdict(status_kept) :- leaves(N), N <= 30.",
      |           "verdict(status_lost_no_card) :- leaves(N), N > 30.",
      |           "verdict(collect_it) :- waits(N), N <= 60.",
      |           "verdict(card_annulled) :- waits(N), N > 60.",
      |           "verdict(permit_kept) :- away(N), N < 6.",
      |           "verdict(six_months_texts_differ) :- away(N), N = 6.",
      |           "verdict(permit_may_be_withdrawn) :- away(N), N > 6.",
      |           "verdict(inform_voivode_in_time) :- moved(N), N <= 15.",
      |           "verdict(inform_voivode_late) :- moved(N), N > 15.",
      |           "verdict(may_appeal) :- refused(N), N <= 14.",
      |           "verdict(appeal_time_passed) :- refused(N), N > 14.",
      |           "verdict(fee_not_returned) :- refused(N).",
      |           "verdict(may_apply_again) :- refused(N), unpaid(yes).",
      |           "verdict(replace_for_200) :- lost(first).",
      |           "verdict(replace_for_300) :- lost(again).",
      |           "verdict(may_work) :- wants(work).",
      |           "verdict(may_run_business) :- wants(business)."],
      | "answers": [{"pattern": "{V:[a-z0-9_]+}", "goal": "verdict(V)", "unordered": false}]}""".stripMargin)
    .fold(sys.error, identity)

  def leaves(n: Int) = s"the application is made and the applicant leaves Poland for $n days"
  def waits(n: Int) = s"the card is ready and $n days have passed since the office said so"
  def away(n: Int) = s"the card is held and the holder is away from Poland for $n months at a stretch"
  def moved(n: Int) = s"the card is held and the holder moved $n working days ago"
  def refused(n: Int) = s"the card was refused and the decision was delivered $n days ago"
  def unpaid(fee: Boolean) = if fee then "the refusal was for a fee not paid" else "the refusal was for another reason"
  def lost(again: Boolean) =
    if again then "the card is held and was lost again by the holder's fault"
    else "the card is held and was lost for the first time"
  val work = "the card is held and the holder wants to work"
  val business = "the card is held and the holder wants to run a business"

  private def answer(sentences: Vector[String]): String =
    Solve.run(reference, sentences).fold(sys.error, _.map(_._2).sorted.mkString(" + "))

  /** the office's own questions (their numbers), as I state them, and what the office answers */
  val official: Vector[(String, Exercise)] = Vector(
    "43: after applying, may I leave Poland — yes, for no longer than 30 days" ->
      Exercise(1, Vector(leaves(30)), "status_kept"),
    "43: … longer: the UKR status is lost (and with it the card: PESEL UKR on the day of issue)" ->
      Exercise(2, Vector(leaves(31)), "status_lost_no_card"),
    "49: the card not collected within 60 days is annulled" -> Exercise(3, Vector(waits(61)), "card_annulled"),
    "49: … on the sixtieth day it can still be collected" -> Exercise(4, Vector(waits(60)), "collect_it"),
    "53, 54: away for more than six months at a stretch — the permit may be withdrawn" ->
      Exercise(5, Vector(away(7)), "permit_may_be_withdrawn"),
    "53: … for less it is kept" -> Exercise(6, Vector(away(5)), "permit_kept"),
    "9, 54: exactly six months — the office's texts differ" -> Exercise(7, Vector(away(6)), "six_months_texts_differ"),
    "51: a new address is told to the voivode within 15 working days" ->
      Exercise(8, Vector(moved(10)), "inform_voivode_in_time"),
    "46, 47: a refusal — an appeal within 14 days, the fee is not returned" ->
      Exercise(9, Vector(refused(5), unpaid(false)), "fee_not_returned + may_appeal"),
    "45: refused for a fee not paid — one may apply again" ->
      Exercise(10, Vector(refused(20), unpaid(true)), "appeal_time_passed + fee_not_returned + may_apply_again"),
    "60: a lost card is replaced for 200 zł" -> Exercise(11, Vector(lost(false)), "replace_for_200"),
    "60: … lost again by one's own fault, for 300 zł" -> Exercise(12, Vector(lost(true)), "replace_for_300"),
    "55: with the card one works with no further permit" -> Exercise(13, Vector(work), "may_work"),
    "56: … and runs a business as Polish citizens do" -> Exercise(14, Vector(business), "may_run_business"))

  /** a case sampled: a situation, its numbers on both sides of each threshold */
  def sampled(number: Int, rnd: Random): Exercise =
    def one[A](xs: A*): A = xs(rnd.nextInt(xs.size))
    val sentences = rnd.nextInt(8) match
      case 0 => Vector(leaves(one(1, 7, 29, 30, 31, 45, 90)))
      case 1 => Vector(waits(one(1, 30, 59, 60, 61, 75, 120)))
      case 2 => Vector(away(one(1, 3, 5, 6, 7, 9, 12)))
      case 3 => Vector(moved(one(1, 10, 14, 15, 16, 30)))
      case 4 | 5 => rnd.shuffle(Vector(refused(one(1, 7, 13, 14, 15, 30)), unpaid(rnd.nextBoolean())))
      case 6 => Vector(lost(rnd.nextBoolean()))
      case _ => Vector(one(work, business))
    Exercise(number, sentences, answer(sentences))
