# okay-migrant

A legal assistant for an immigrant: **https://okay-effects.github.io/okay-migrant/** — a few questions, then an
exact answer from a country's official rules for foreigners, with the office's own sentence, where it stands and
the day it was read. In Ukrainian, Russian, Polish and English. **Information, not legal advice.**

In Poland, for now:
- **the CUKR residence card** (for holders of the UKR status; applications until 4 March 2027): whether you may
  apply, and what follows — leaving after the application, collecting the card, being away, moving, a refusal, a
  lost card, work;
- **PESEL and PESEL UKR**: who may get PESEL UKR and how (30 days from the border, a passport, the exclusions),
  what ends the UKR status, how a foreigner gets an ordinary PESEL, and the difference between the two;
- **the temporary residence and work permit** (the single permit): where you are and on what basis, the
  purpose, the pay (4806 zł gross in 2026, all employers together), insurance, the starost's list;
- **the permanent residence permit** (pobyt stały): by the law's grounds — a spouse of a Polish citizen, Karta
  Polaka, Polish origin, protection, a tolerated stay, a child…;
- **the long-term EU resident permit**: five years and how they count, absences, income, insurance, Polish B1.

## How it is made

- **The rules are a model checked against the office.** `books/pl/` holds the Office for Foreigners' own
  documents as published (with where from and the day fetched). From them a language model wrote the rules as
  data — sentences, Datalog, answers — by [okay-study]; each model (`models/`) is kept under tests: the office's
  own questions stated as cases, cases it was never shown, and a second reading made apart, on three thousand
  cases.
- **The page answers by an exact engine**, never from a model's memory: there is no language model in it and no
  server. What you answer stays in the page.
- **Only what can change the answer is asked**; nothing is assumed; every reason is found, not the first.
- **Every official sentence shown stands in the published document** — a test holds it there. The plain wordings
  beside them are ours and unofficial.
- Where the office's texts disagree (an absence of exactly six months), the assistant shows both and does not
  choose.

```
sbt test                                                    # no model needed
sbt "coreJVM/runMain okay.migrant.Main uk"                  # the assistant in a terminal: uk | ru | pl | en
sbt web && scala-cli run scripts/page.scala -- app/.js/target/web runs/page.png   # the page, driven in Chromium
```

The law changes: a book is fetched again before it is relied on, and every answer carries the day of its source.
Another topic or another country is another book — an official text, its cases, a model checked against them
(`AGENTS.md`, `specs/assistant.md`).

Built on [okay-effects](https://github.com/okay-effects): [okay-study], [okay-datalog], [okay-scala], [okay-ui].

[okay-study]: https://github.com/okay-effects/okay-study
[okay-datalog]: https://github.com/okay-effects/okay-datalog
[okay-scala]: https://github.com/okay-effects/okay-scala
[okay-ui]: https://github.com/okay-effects/okay-ui
