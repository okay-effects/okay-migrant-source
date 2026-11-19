# AGENTS.md — okay-migrant

A legal assistant for an immigrant: a country's rules for foreigners, read from its OFFICIAL texts into exact
models, and an assistant that asks for the facts a person's case lacks and answers with the rule's source and its
date. The method and its library are okay-study (`~/work/my/okay-study`: read its `specs/study.md` — the model
as data, the study by a teacher, the book as the judge). Scala 3, lines at most 120 characters, English in the
repository; the assistant speaks the people's languages (uk, ru, pl, en first).

- `books/<country>/<topic>/`: the official sources AS PUBLISHED, never edited, with `SOURCE.md` (where from, the
  day fetched); `lesson.txt` and `exercises.jsonl` made from them (`Cukr.main`). First: `books/pl/cukr/`.
- `core/` (`okay.migrant`): a topic's cases — the canonical sentences, the reference reading, the official cases
  stated by hand with the office's answers (`Cukr`), and its tests. A reference is OUR reading: it serves by the
  official cases it answers as the office does.
- `models/<country>-<topic>.json`: the model a teacher wrote from the lesson (okay-study's `Judge` with
  `BOOK=books/pl/cukr`), under a test against the official cases and against the reference.
- The rules of this project:
  1. Nothing is answered from a model's memory: only what the engine derives from a model checked against the
     official cases.
  2. Every answer shown to a person carries its source, the source's date, and that it is information, not legal
     advice; what the model does not cover is said, not guessed.
  3. The law changes: a book is fetched again before it is relied on; the day is part of the data.
  4. A person's facts stay on the device.
- Tests: `sbt test` (no model). okay-study comes from ivy-local for now (`sbt core/publishLocal
  study/publishLocal` there).
- Plans: `SPRINT.md`; results: `specs/assistant.md`. Commits: plain messages, no attribution trailers.
