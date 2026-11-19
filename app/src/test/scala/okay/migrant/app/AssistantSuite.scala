package okay.migrant.app

import okay.business.scala3.*

/** the page's program played by its buttons' labels (okay-ui's `Play`), no browser */
class AssistantSuite extends munit.FunSuite:
  private def played(acts: Act*) = Play(steps(Assistant.main[Ui](20261119)), acts*)

  test("in Russian: a person who came, the status under a year — the reason, then again from the languages"):
    val p = played(Act.press("Русский"), Act.press("Могу ли я подать заявление на карту CUKR?"),
      Act.press("Для человека, который приехал в Польшу из Украины после 23 февраля 2022 г."),
      Act.press("Да"), Act.press("Да"), Act.press("Меньше 365 дней, или был перерыв (периоды не складываются)"),
      Act.press("Сегодня"), Act.press("Да"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Сейчас подать не получится"))
    val again = played(Act.press("Русский"), Act.press("Могу ли я подать заявление на карту CUKR?"),
      Act.press("Для человека, который приехал в Польшу из Украины после 23 февраля 2022 г."),
      Act.press("Да"), Act.press("Да"), Act.press("Меньше 365 дней, или был перерыв (периоды не складываются)"),
      Act.press("Сегодня"), Act.press("Да"), Act.press("Ещё раз"))
    assertEquals(again.at.map(_.title), Some("CUKR"))

  test("in Ukrainian: a child born in Poland whose mother holds the card — may apply, three questions only"):
    val p = played(Act.press("Українська"), Act.press("Чи можу я подати заяву на посвідку CUKR?"),
      Act.press("Для дитини, народженої в Польщі після 23 лютого 2022 р."),
      Act.press("Так"), Act.press("Так"), Act.press("Сьогодні"), Act.press("Так"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Можна подавати"))

  test("in Polish: the card held, away for exactly six months — the page says the office's texts differ"):
    val p = played(Act.press("Polski"), Act.press("Mam kartę — na jak długo mogę wyjechać z Polski?"),
      Act.press("Dokładnie 6 miesięcy"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Mam kartę — na jak długo mogę wyjechać z Polski?"))

  test("in English: refused for a fee not paid, ten days ago — appeal, no refund, apply again"):
    val p = played(Act.press("English"), Act.press("I was refused the card"), Act.press("14 days or fewer"),
      Act.press("For a fee not paid"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("I was refused the card"))
