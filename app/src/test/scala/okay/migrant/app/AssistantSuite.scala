package okay.migrant.app

import okay.business.scala3.*

/** the page's program played by its buttons' labels (okay-ui's `Play`), no browser */
class AssistantSuite extends munit.FunSuite:
  private def played(acts: Act*) = Play(steps(Assistant.main[Ui](20261119)), acts*)

  test("in Russian: a person who came, the status under a year — the reason, then again from the languages"):
    val p = played(Act.press("Русский"),
      Act.press("Для человека, который приехал в Польшу из Украины после 23 февраля 2022 г."),
      Act.press("Да"), Act.press("Да"), Act.press("Меньше 365 дней, или был перерыв (периоды не складываются)"),
      Act.press("Сегодня"), Act.press("Да"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Сейчас подать не получится"))
    val again = played(Act.press("Русский"),
      Act.press("Для человека, который приехал в Польшу из Украины после 23 февраля 2022 г."),
      Act.press("Да"), Act.press("Да"), Act.press("Меньше 365 дней, или был перерыв (периоды не складываются)"),
      Act.press("Сегодня"), Act.press("Да"), Act.press("Ещё раз"))
    assertEquals(again.at.map(_.title), Some("CUKR"))

  test("in Ukrainian: a child born in Poland whose mother holds the card — may apply, three questions only"):
    val p = played(Act.press("Українська"), Act.press("Для дитини, народженої в Польщі після 23 лютого 2022 р."),
      Act.press("Так"), Act.press("Так"), Act.press("Сьогодні"), Act.press("Так"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Можна подавати"))
