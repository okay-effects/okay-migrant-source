package okay.migrant.app

import okay.business.scala3.*

/** the page's program played by its buttons' labels (okay-ui's `Play`), no browser */
class AssistantSuite extends munit.FunSuite:
  private def played(acts: Act*) = Play(steps(Assistant.main[Ui](20261119)), acts*)

  test("in Russian: a person who came, the status under a year — the reason, then again from the languages"):
    val p = played(Act.press("Русский"), Act.press("Карта CUKR (для граждан Украины со статусом UKR)"), Act.press("Могу ли я подать заявление на карту CUKR?"),
      Act.press("Для человека, который приехал в Польшу из Украины после 23 февраля 2022 г."),
      Act.press("Да"), Act.press("Да"), Act.press("Меньше 365 дней, или был перерыв (периоды не складываются)"),
      Act.press("Сегодня"), Act.press("Да"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Сейчас подать не получится"))
    val again = played(Act.press("Русский"), Act.press("Карта CUKR (для граждан Украины со статусом UKR)"), Act.press("Могу ли я подать заявление на карту CUKR?"),
      Act.press("Для человека, который приехал в Польшу из Украины после 23 февраля 2022 г."),
      Act.press("Да"), Act.press("Да"), Act.press("Меньше 365 дней, или был перерыв (периоды не складываются)"),
      Act.press("Сегодня"), Act.press("Да"), Act.press("Ещё раз"))
    assertEquals(again.at.map(_.title), Some("Polska · Польща · Польша · Poland"))

  test("in Ukrainian: a child born in Poland whose mother holds the card — may apply, three questions only"):
    val p = played(Act.press("Українська"), Act.press("Посвідка CUKR (для громадян України зі статусом UKR)"), Act.press("Чи можу я подати заяву на посвідку CUKR?"),
      Act.press("Для дитини, народженої в Польщі після 23 лютого 2022 р."),
      Act.press("Так"), Act.press("Так"), Act.press("Сьогодні"), Act.press("Так"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Можна подавати"))

  test("in Polish: the card held, away for exactly six months — the page says the office's texts differ"):
    val p = played(Act.press("Polski"), Act.press("Karta pobytu CUKR (dla obywateli Ukrainy ze statusem UKR)"), Act.press("Mam kartę — na jak długo mogę wyjechać z Polski?"),
      Act.press("Dokładnie 6 miesięcy"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Mam kartę — na jak długo mogę wyjechać z Polski?"))

  test("in English: refused for a fee not paid, ten days ago — appeal, no refund, apply again"):
    val p = played(Act.press("English"), Act.press("The CUKR card (for Ukrainians with the UKR status)"), Act.press("I was refused the card"), Act.press("14 days or fewer"),
      Act.press("For a fee not paid"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("I was refused the card"))

  test("in Ukrainian: permanent residence, a spouse married for two years — under three, and no two years on the permit"):
    val p = played(Act.press("Українська"), Act.press("Дозвіл на постійне проживання (pobyt stały)"),
      Act.press("Я у шлюбі з громадянином / громадянкою Польщі"), Act.press("Менше 3 років"), Act.press("Ні"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("За цією підставою — ні"))

  test("in Russian: the EU resident — everything met; the fee and the housing to remember"):
    val p = played(Act.press("Русский"), Act.press("Разрешение долгосрочного резидента ЕС"),
      Act.press("Работа, бизнес, семья, постоянное разрешение или другое временное разрешение"),
      Act.press("5 лет или больше"), Act.press("6 месяцев или меньше"), Act.press("10 месяцев или меньше"),
      Act.press("Да"), Act.press("Да"), Act.press("Есть документ о B1 или выше (сертификат, аттестат польской школы и т. п.)"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Можно подавать"))
