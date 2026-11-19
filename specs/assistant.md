# okay-migrant: the assistant and its books

## 1. The first book: the CUKR residence card, Poland (2026-11-19)

Useful and plain to people, where Carroll is neither. First slice: **the CUKR residence card** (Poland; a
temporary residence permit for holders of the UKR status, applications 4 May 2026 – 4 March 2027). Other
countries and topics after: a book a directory (`books/<country>/<topic>/`: the sources as published with `SOURCE.md`, the
lesson, the exercises), a model a file.

**The book** (`books/pl/cukr/`, fetched 2026-11-19): the Office for Foreigners' information sheet (3 pages) and its
sixty official questions with answers, in Polish and Ukrainian. Questions 11–20 are cases.

**The cases** (`Cukr`): a case is a few canonical sentences about an applicant (came from Ukraine, or a child
born in Poland; PESEL UKR active on the day of the application, on 4 June 2025; the days of UKR status without a
break; the day of the application as a number; the mother's card; the registers); the answer `eligible` or the
reasons it fails (`no_pesel_today`, `no_pesel_june_2025`, `under_365_days`, `too_early`, `too_late`,
`mother_without_card`, `registers_incomplete`). The reference model is MY reading of the sheet and of answers
6–20, 29; it answers **UDSC's own ten cases, stated by hand, as UDSC does** (`CukrSuite`) — the check of that
reading, and its limit: the statement of each official case is mine too.

### 1.1 The theory by the teacher — 48 of 48 held out, the official cases 10 of 10

The lesson: the sheet and the official answers 1–10, in Polish, as published (20 KB). Sixty sampled cases, 12
shown, 48 held out; the strong teacher, a fresh agent, the task's one file:
- **Words: first writing.** But the thresholds went into the notation: fifteen term forms that sort a date and a
  number of days by regular expressions (`{N:36[5-9]}` → enough) — the words' task did not say that rules can
  compare numbers. Right, and not what a notation is for; the task now says a threshold belongs to a rule.
- **Rules: first writing — shown 12 of 12, HELD OUT 48 of 48** (`models/pl-cukr.json`). On UDSC's ten cases: 10 of
  10. Against the reference on three thousand cases over any day of 2025–2028 and any length of status: no
  difference (`CukrTeacherSuite`). Two readings made apart — mine and the teacher's — agree.

What this is not yet: the day the card is issued (PESEL UKR must still be active then) is in no case — it cannot
be known when applying, and has to be SAID with every `eligible`; the questions about what the card changes
(work, travel, benefits, the 60 days, the six months away) are not modelled; nothing reads a person's own words.
Every answer shown to a person carries the source and its date, and that it is information, not legal advice.
