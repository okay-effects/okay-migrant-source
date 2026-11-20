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

## 2. The conversation (2026-11-19)

`Topic` (a model checked against the office's cases; the questions that state a case, each answer a canonical
sentence; each verdict in plain words with its official source), `Interview.run` — a program of the effect
`Person` alone — and `CukrTopic`.

- **What is asked is decided by the exact side** (`Topic.next`): a question is asked only while some answer to it
  can still change what the model answers, over every way the other open questions may be answered (okay-desk
  theory §10 b: decide only what changes the answer). No order of questions is written per topic: a child born in
  Poland is never asked about 4 June 2025 or the 365 days, a person who came is never asked about the mother.
  When nothing can change the answer, it is given. All the reasons are found, not the first: after "less than
  365 days" the day of the application is still asked.
- **Nothing is assumed**: a fact is in the case only because the person said it; an answer that is none of the
  question's asks it again; the question about the registers has no "I do not know" that would count as yes.
- **A value**: the day of the application may be chosen (today; before, within, after the window) or typed
  (`dd.mm.yyyy`), and becomes the number the model compares.
- **The answer** (`Outcome.said`): each verdict in the person's language (ours, unofficial), under it the office's
  own sentence as published — Polish or Ukrainian, the languages the office wrote in — and its place; beside a
  positive answer what cannot be checked now (PESEL UKR on the day of issue) and that the application is only
  electronic; the source, the day it was fetched, and that it is information, not legal advice.

**Checks** (`InterviewSuite`): the office's ten cases told by answering what is asked give the office's answers;
only what can change the answer is asked; EVERY official sentence shown stands in the book as published (the
law of quotes, over both languages); an answer carries its source, its day and what it is not.
Try it: `sbt "core/runMain okay.migrant.Main ru"` (uk | ru | pl | en; answers may follow: `… Main ru 1 1 1 2 1 1`).

Not yet: a person's own words (the answers are chosen, or a date typed); the page; what the card changes.

## 3. The page (2026-11-19)

`app/` — `Assistant.main`, a program of okay-ui's `Ui` alone: the language (uk, ru, pl, en), the conversation of
§2 with a question a screen and an answer a button (`Interview.by`, the asking given), the answer — each verdict
with the office's own sentence and its place, what to remember, the source and its day, "information, not legal
advice" — and again (no Quit). The model the page runs is `models/pl-cukr.json` as text (`Models`, made by the
build), run by okay-study's core compiled to JavaScript: no server, no request but the page's own files.

- Played with no browser (`AssistantSuite`, okay-ui's `Play`, by the buttons' labels): a refusal in Russian and
  back to the languages; a child born in Poland in Ukrainian — may apply.
- In a browser at a phone's size (`scripts/page.scala`, Playwright, the built page served from its directory):
  the same two conversations, the official sentences on the screen, the foot ("Built on okay-effects", "Need
  more?"), no console error, and NO REQUEST LEAVING THE PAGE but the analytics beacon (answered by an empty
  script in the test).
- `sbt web` builds `app/.js/target/web` (index.html, main.js — 0.8 MB); `sbt publishSite` pushes it from a clean
  tree to the public repository of the built page (`-Dsite.repo=…`), as the other applications do.

**Published 2026-11-19**: https://okay-effects.github.io/okay-migrant/ — the page's repository
okay-effects/okay-migrant (public, GitHub Pages, branch main), the source okay-effects/okay-migrant-source
(public since 2026-11-20). The live page answers the two conversations of `scripts/page.scala` (run with the address). Its section
is on okay-effects.github.io (a nav link, "What it does / How it is built", the libraries' line).

## 4. What follows the application: the second book (2026-11-20)

The same sources (`books/pl/cukr/`), another lesson — the official answers 2–5, 9 and 43–60, as published
(`books/pl/cukr-after/lesson.txt`, 15 KB) — and its own cases (`CukrAfter`): a situation in one or two canonical
sentences (the applicant leaves for N days after applying; the card is ready and N days have passed; the holder is
away N months at a stretch; moved N working days ago; a refusal delivered N days ago, for a fee or not; the card
lost, for the first time or again; work, a business), the answer what follows (seventeen verdicts).

- **The reference** (my reading) answers the office's fourteen questions as the office does (`CukrAfterSuite`).
- **One place the office's texts disagree, and the model does not choose**: an absence of exactly six months —
  "powyżej 6 miesięcy" (the sheet; answer 9), "co najmniej 6 miesięcy" (answer 54), "jednorazowo na okres 6
  miesięcy" (answer 53). Less than six: the permit is kept; more: it may be withdrawn; exactly six: the assistant
  says the texts differ, shows both sentences, and to ask the office. Reading a book closely finds this.
- **The teacher's theory** (a fresh agent, the task's one file; 25 cases shown — the fewest that hold every
  verdict's name twice, since a name cannot be guessed — 19 held out): words and rules at the first writing,
  **shown 25 of 25, held out 19 of 19**; the office's fourteen questions; no difference from the reference on
  three thousand cases with any number from 0 to 400 (`models/pl-cukr-after.json`, `CukrAfterTeacherSuite`).
- **In the assistant** (`CukrAfterTopic`): seven situations, each a `Topic` over the one model, beside "can I
  apply" — after the language the page asks which. A number is answered by its side of the threshold. A verdict
  may rest on two official sentences (`Reason.also`): both are shown. `Reason.good` tells what the person hoped
  for from what they did not; the title of an answer is the situation's.
- Found on the way: the lessons had lost the first line of every page (a form feed begins it and the cut dropped
  the line); they are kept now — the first book's lesson gained six lines, its model was written without them.

Checks: 73 tests; the law of quotes over every topic and both sources of a verdict; `scripts/page.scala` — four
conversations in Chromium at a phone's size (a refusal in Russian, a child in Ukrainian, exactly six months in
Polish with both of the office's sentences on the screen, a refusal for a fee in English).

## 5. Beyond CUKR: the permanent residence permit and the long-term EU resident permit (2026-11-20)

The operator asked for the assistant to be general, not CUKR alone. Two permits most foreigners in Poland ask
about, from the Office for Foreigners' MOS portal (`books/pl/permanent/`, `books/pl/eu-resident/`: the information
page in Polish, Ukrainian and Russian and the official questions and answers, as published, CC BY-NC-ND 3.0 PL).

- **Permanent residence** (`Permanent`): a ground of the law's list (a spouse of a Polish citizen: 3 years of
  marriage and 2 years on a temporary permit for it right before; Karta Polaka or Polish origin and the will to
  settle; a child of a Polish citizen; a child of a settled foreigner born at the right time; refugee or subsidiary
  or humanitarian: 5 years; tolerated: 10; asylum; the desired occupation — not possible yet, no regulation;
  none) and what it asks for. Eleven of the office's statements as cases.
- **Long-term EU resident** (`EuResident`): the basis at the time of applying (studies, temporary protection,
  humanitarian, tolerated, asylum, a protection procedure, intra-corporate transfer exclude it), the counted stay
  (5 years; the question says how to count: studies half, temporary protection and school not at all), the
  longest absence (6 months) and all together (10), income for 3 years over the threshold (from 2025: 1010 zł
  alone, 823 per family member), insurance, Polish B1 by a document (not under 16). Eleven statements as cases.
- **The teachers' theories**: words at the second writing in both (the first time the goals were atoms with no
  arguments — the engine has none, the judge told them; one teacher also had not been shown five kinds of
  sentence: `Books.write` now shows every kind of sentence, its numbers aside, as well as every verdict), rules at
  the first: **permanent 18 shown, 12 of 12 held out; EU resident 12 shown, 106 of 106 held out**; every
  statement of the office; on three thousand sampled cases each no difference from the reference wherever the
  reference reads the case (the teachers made the law's fixed numbers — 16 years, 2 years — slots, a wider
  language than the reference's).
- **On the page**: after the language, an area — the CUKR card (its eight topics), permanent residence, the EU
  resident. Quotes now also in Russian (the portal's own version). The law of quotes runs over every book.

Checks: 125 tests; `scripts/page.scala` — six conversations in Chromium at a phone's size, the two new permits
among them. Not modelled: the documents, the procedure, appeals, the Blue Card's "2+3" rule and its 12/18-month
breaks, the income's exact sums (asked as yes or no with the thresholds said).

## 6. The single permit: temporary residence and work (2026-11-20)

The permit most working foreigners need (`books/pl/work/`: the MOS information page in pl/uk/ru and the official
questions and answers in pl/uk/ru). `Work`: where the person is and on what basis (an application from abroad is
not admissible; a tourist, family-visit, student, transit or medical visa excludes it), the purpose (work of more
than 3 months; seasonal work, a posting by a foreign employer and one's own business have other permits), the
gross monthly pay from all employers together (at least 4806 zł in 2026, whatever the hours), pay not under that
of comparable workers, insurance, the starost's list of closed occupations (except the same job continued with
the same employer, and Turkish citizens). Thirteen of the office's statements as cases.

- **The teacher**: words at the second writing — the third teacher in a row wrote answers as atoms with no
  arguments; okay-study's format now says a goal has at least one argument (republished 0.1.0) — rules at the
  first: **12 shown, 77 of 77 held out**, the thirteen statements, no difference from the reference on three
  thousand cases (`models/pl-work.json`).
- **On the page**: a fourth area, before permanent residence. The answer reminds of the fees (440 + 100 zł), the
  employer's annex no. 1 in MOS, the 15 days to report a lost job, and that a change of the named employer is a
  change of the permit.
- The law of quotes caught one of mine: a Russian quote with "злотих" for the portal's "злотых".

Checks: 140 tests; the browser — seven conversations, the single permit's pay under the minimum among them.

## 7. PESEL and PESEL UKR (2026-11-20)

`books/pl/pesel/`: MSWiA's questions and answers on the UKR status (PDF and its text) and the City of Warsaw's
two pages — PESEL with the UKR status, and PESEL on a foreigner's application. One model, three situations
(`Pesel`): getting PESEL UKR (who — a Ukrainian who lived in Ukraine before 24 February 2022, a foreigner with
protection there, a foreigner who stayed there legally and cannot go home safely, their families; within 30 days
of crossing the border; a valid passport; not with another permit or protection in Poland, an application for
international protection, temporary protection in another EU state, or a local border traffic permit), keeping
the status (away more than 30 days at a stretch, another permit, an application for protection, protection
elsewhere in the EU, an EU citizenship, no passport shown by 31 August 2026 end it), and an ordinary PESEL (by
registering one's residence, or on application, in person, when a law requires the number). Fifteen statements.

- **The teacher**: words and rules both at the first writing — **27 shown, 37 of 37 held out**, the fifteen
  statements, no difference from the reference on three thousand cases (`models/pl-pesel.json`).
- **On the page**: an area "PESEL and PESEL UKR" with three topics, after CUKR. Every answer says the difference:
  PESEL UKR confirms temporary protection (a legal stay until 4 March 2027, work with no permit — the UdSC's own
  sentence); an ordinary PESEL is only a number and no basis of stay.

Checks: 180 tests; the browser — eight conversations, PESEL UKR in Ukrainian among them.

## 8. A link to every screen (2026-11-20)

The user's request: every page of the app has a link that can be sent and opens where it should. The page's
program says where it is by an effect, `Address` (`Opened`: where the page was opened; `Now(path)`: the screen
shown); the browser's handler keeps it in the URL's fragment, each screen a new entry of the history, so Back goes
to the screen before. A path: the language, the area (`cukr`, `pesel`, `work`, `permanent`, `eu-resident`), the
topic of an area of several (its English name as words of a link), the answers given as `question=choice` —
`#/uk/pesel/how-to-get-pesel-ukr/who=ua/crossed=in/passport=yes/excluded=none` opens at that answer. A link's
answers are given to the conversation (`Interview.along`), not asked; what no longer exists in a link is skipped
and the nearest screen shown. The header's button sends the link by the phone's own sharing, or copies it; the
foot says a link carries the answers that lead to it. The fragment never goes to the server.

The effect moved into okay-ui the same day (`Address`: `whereOpened`, `nowAt`; `addressed` for tests, `InThePage.address`
and `InThePage.onBack` in a page; okay-ui 0.1.0 republished), the app's own copy removed.

Checks: the program played with an address (each screen's path; a link opened at its answer; an old link at the
nearest screen); the browser — the link opened in another page at the same answer, Back, the button's words.

## 9. Everything in the person's language (2026-11-20)

The user saw Polish on the Ukrainian page. 37 of the 72 official sentences shown were published in Polish alone
(62 for Russian, all for English), and the sources' titles were Polish. `Translations`: our translation of each,
by its Polish text, and of each title. Shown: the office's own text where it published one in the language, else
our translation with "unofficial translation; in Polish: «…»" and the original. The foot's note follows the
screen's language. Checks: every source shown has, in uk/ru/en, the office's text or our translation, every title
its translation, no translation of what is not shown (`InterviewSuite`); the browser — the PESEL UKR answer in
Ukrainian with the translation and its original.

## 10. I have come to Poland: what do I do? (2026-11-20)

The user asked for a guide for every case of arriving. `books/pl/arrival/` (MOS, pl/uk/ru: entry, visa-free travel,
visas, a permit's purposes, studies, an EU citizen's registration — Polish only —, the rules of work, Ukraine's
protection, family life) with the books already here. A case: who — from Ukraine because of the war, an EU/EEA/Swiss
citizen, from elsewhere without a visa, on a Schengen visa, on a Polish national visa —, how long (90 days or more),
what for (work, full-time studies, a Polish spouse, other). The answer is the STEPS in order (`s1_…` … `s9_…`), each
with the office's sentence; a step the assistant says more of (`Reason.see`: PESEL UKR, keeping the status, CUKR,
the single permit, permanent residence, an ordinary PESEL) gives a button that opens that topic. Only what changes the
steps is asked: from Ukraine, nothing more; an EU citizen, how long; a national visa, not how long.

- **My reading corrected by the teacher**: the single permit's exclusion names visas "typu D lub Schengen" issued for
  tourism or a visit — my reference had it for Schengen visas alone; the teacher's first rules had it for both.
- **The teacher**: words at the first writing; rules shown 12 — 12 of 12 shown, but 10 of 27 held out: twelve
  cases do not show a table this wide. Shown 24 with the judge's report: **24 of 24, 15 of 15 held out**, the eleven
  statements, no difference from the reference on three thousand cases (`models/pl-arrival.json`).
- 14 new Polish sentences and 6 titles translated (`Translations`), in "ви/вы".

Checks: 222 tests; the browser — nine conversations, the arrival in Russian (visa-free, longer, work) and its "more".
Other countries: each needs its official books first; none is in yet.
