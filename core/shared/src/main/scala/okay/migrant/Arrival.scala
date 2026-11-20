package okay.migrant

import okay.study.{Exercise, Model, Solve}

import scala.util.Random

/** I HAVE COME TO POLAND: WHAT DO I DO? (`books/pl/arrival/` with `cukr/`, `pesel/`, `work/`, `permanent/`; the
 * Office for Foreigners' MOS portal, MSWiA, fetched 2026-11-20). A case: who the person is — from Ukraine because of
 * the war, an EU citizen, or from outside the EU without a visa, on a Schengen visa, on a Polish national visa — how
 * long they mean to stay, and what for. The answer: the STEPS, each a verdict, their names ordered as the steps are
 * taken (`s1_…`, `s2_…`, `s9_…` last). The reference is MY reading; `official` are the offices' statements, as cases */
object Arrival:
  val reference: Model = Model.parse(
    """{"sentences": [
      |  {"pattern": "the person came from Ukraine because of the war that began on 24 February 2022, or is a family member of such a person", "facts": ["who(ukraine)"]},
      |  {"pattern": "the person is a citizen of an EU state, Norway, Iceland, Liechtenstein or Switzerland", "facts": ["who(eu)"]},
      |  {"pattern": "the person came without a visa", "facts": ["who(visa_free)"]},
      |  {"pattern": "the person came on a Schengen visa", "facts": ["who(schengen)"]},
      |  {"pattern": "the person came on a Polish national visa", "facts": ["who(national)"]},
      |  {"pattern": "the person plans to stay in Poland more than 90 days", "facts": ["long(yes)"]},
      |  {"pattern": "the person plans to stay in Poland 90 days or less", "facts": ["long(no)"]},
      |  {"pattern": "the person's purpose is work", "facts": ["purpose(work)"]},
      |  {"pattern": "the person's purpose is full-time studies at a university", "facts": ["purpose(studies)"]},
      |  {"pattern": "the person's purpose is life with a spouse who is a Polish citizen", "facts": ["purpose(spouse)"]},
      |  {"pattern": "the person's purpose is something else", "facts": ["purpose(other)"]}],
      | "terms": [],
      | "rules": ["step(s1_pesel_ukr_in_30_days) :- who(ukraine).",
      |           "step(s2_ukr_work_without_permit) :- who(ukraine).",
      |           "step(s3_ukr_away_at_most_30_days) :- who(ukraine).",
      |           "step(s4_ukr_cukr_after_365_days) :- who(ukraine).",
      |           "step(s1_eu_three_months_free) :- who(eu).",
      |           "step(s2_eu_register_stay) :- who(eu), long(yes).",
      |           "step(s3_eu_work_free) :- who(eu).",
      |           "outside(yes) :- who(visa_free).",
      |           "outside(yes) :- who(schengen).",
      |           "outside(yes) :- who(national).",
      |           "step(s1_count_90_in_180_days) :- who(visa_free).",
      |           "step(s1_count_90_in_180_days) :- who(schengen).",
      |           "step(s2_work_needs_a_permit) :- outside(yes), purpose(work).",
      |           "staying(yes) :- outside(yes), long(yes).",
      |           "staying(yes) :- who(national), purpose(P).",
      |           "step(s3_apply_while_stay_is_legal) :- staying(yes).",
      |           "step(s4_single_permit) :- staying(yes), purpose(work).",
      |           "step(s5_visa_purpose_may_exclude) :- staying(yes), purpose(work), who(schengen).",
      |           "step(s5_visa_purpose_may_exclude) :- staying(yes), purpose(work), who(national).",
      |           "step(s4_study_permit) :- staying(yes), purpose(studies).",
      |           "step(s5_studies_work_without_permit) :- staying(yes), purpose(studies).",
      |           "step(s4_spouse_permit) :- staying(yes), purpose(spouse).",
      |           "step(s5_permanent_later) :- staying(yes), purpose(spouse).",
      |           "step(s4_other_purposes) :- staying(yes), purpose(other).",
      |           "step(s9_register_residence_pesel) :- staying(yes).",
      |           "step(s9_register_residence_pesel) :- who(eu), long(yes).",
      |           "verdict(V) :- step(V)."],
      | "answers": [{"pattern": "{V:[a-z0-9_]+}", "goal": "verdict(V)", "unordered": false}]}""".stripMargin)
    .fold(sys.error, identity)

  val ukraine = "the person came from Ukraine because of the war that began on 24 February 2022, or is a family " +
    "member of such a person"
  val eu = "the person is a citizen of an EU state, Norway, Iceland, Liechtenstein or Switzerland"
  val visaFree = "the person came without a visa"
  val schengen = "the person came on a Schengen visa"
  val national = "the person came on a Polish national visa"
  def long(yes: Boolean) =
    if yes then "the person plans to stay in Poland more than 90 days"
    else "the person plans to stay in Poland 90 days or less"
  val work = "the person's purpose is work"
  val studies = "the person's purpose is full-time studies at a university"
  val spouse = "the person's purpose is life with a spouse who is a Polish citizen"
  val other = "the person's purpose is something else"

  private def answer(sentences: Vector[String]): String =
    Solve.run(reference, sentences).fold(sys.error, _.map(_._2).sorted.mkString(" + "))

  /** the offices' own statements, as I state them as cases, and what follows */
  val official: Vector[(String, Exercise)] = Vector(
    "MSWiA, UdSC: from Ukraine because of the war — PESEL UKR within 30 days, work, at most 30 days away, CUKR later" ->
      Exercise(1, Vector(ukraine), "s1_pesel_ukr_in_30_days + s2_ukr_work_without_permit + " +
        "s3_ukr_away_at_most_30_days + s4_ukr_cukr_after_365_days"),
    "UdSC: an EU citizen for up to 3 months — no formalities but registering an address; work freely" ->
      Exercise(2, Vector(eu, long(false)), "s1_eu_three_months_free + s3_eu_work_free"),
    "UdSC: an EU citizen staying longer registers the stay; the address registered too" ->
      Exercise(3, Vector(eu, long(true)),
        "s1_eu_three_months_free + s2_eu_register_stay + s3_eu_work_free + s9_register_residence_pesel"),
    "MOS: visa-free, a short visit — 90 days in any 180" ->
      Exercise(4, Vector(visaFree, long(false), other), "s1_count_90_in_180_days"),
    "MOS: to work, a foreigner from outside the EU needs a permit, a declaration or a basis that frees them" ->
      Exercise(5, Vector(visaFree, long(false), work), "s1_count_90_in_180_days + s2_work_needs_a_permit"),
    "MOS: staying to work — the single permit, applied for while the stay is legal" ->
      Exercise(6, Vector(visaFree, long(true), work), "s1_count_90_in_180_days + s2_work_needs_a_permit + " +
        "s3_apply_while_stay_is_legal + s4_single_permit + s9_register_residence_pesel"),
    "MOS: … on a Schengen visa (or a national one), its purpose may exclude the single permit" ->
      Exercise(7, Vector(schengen, long(true), work), "s1_count_90_in_180_days + s2_work_needs_a_permit + " +
        "s3_apply_while_stay_is_legal + s4_single_permit + s5_visa_purpose_may_exclude + s9_register_residence_pesel"),
    "MOS: a student on a national visa — the permit for studies; studies give access to work" ->
      Exercise(8, Vector(national, studies), "s3_apply_while_stay_is_legal + s4_study_permit + " +
        "s5_studies_work_without_permit + s9_register_residence_pesel"),
    "MOS: a spouse of a Polish citizen — a temporary permit, work without a permit, permanent residence later" ->
      Exercise(9, Vector(national, spouse), "s3_apply_while_stay_is_legal + s4_spouse_permit + s5_permanent_later + " +
        "s9_register_residence_pesel"),
    "MOS: another purpose — the permit's purposes listed" ->
      Exercise(10, Vector(national, other), "s3_apply_while_stay_is_legal + s4_other_purposes + " +
        "s9_register_residence_pesel"),
    "MOS: work on a national visa — the single permit; a visa D issued for tourism or a visit may exclude it" ->
      Exercise(11, Vector(national, work), "s2_work_needs_a_permit + s3_apply_while_stay_is_legal + " +
        "s4_single_permit + s5_visa_purpose_may_exclude + s9_register_residence_pesel"))

  /** a case sampled: who, how long, what for — every one of them said */
  def sampled(number: Int, rnd: Random): Exercise =
    def one[A](xs: A*): A = xs(rnd.nextInt(xs.size))
    val sentences = Vector(one(ukraine, eu, visaFree, schengen, national), long(rnd.nextBoolean()),
      one(work, studies, spouse, other))
    Exercise(number, rnd.shuffle(sentences), answer(sentences))
