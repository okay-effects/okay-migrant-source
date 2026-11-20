package okay.migrant.app

import okay.business.scala3.*

/** the page's program played by its buttons' labels (okay-ui's `Play`), no browser */
class AssistantSuite extends munit.FunSuite:
  private def played(acts: Act*) = Play(steps(addressed[(Ui + Address), Ui, Unit](Nil,
    collection.mutable.ArrayBuffer.empty[List[String]])(Assistant.main[(Ui + Address)](20261119))), acts*)

  test("in Russian: a person who came, the status under a year — the reason, then again from the languages"):
    val p = played(Act.press("Русский"), Act.press("Карта CUKR (для граждан Украины со статусом UKR)"),
      Act.press("Могу ли я подать заявление на карту CUKR?"),
      Act.press("Для человека, который приехал в Польшу из Украины после 23 февраля 2022 г."),
      Act.press("Да"), Act.press("Да"), Act.press("Меньше 365 дней, или был перерыв (периоды не складываются)"),
      Act.press("Сегодня"), Act.press("Да"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Сейчас подать не получится"))
    val again = played(Act.press("Русский"), Act.press("Карта CUKR (для граждан Украины со статусом UKR)"),
      Act.press("Могу ли я подать заявление на карту CUKR?"),
      Act.press("Для человека, который приехал в Польшу из Украины после 23 февраля 2022 г."),
      Act.press("Да"), Act.press("Да"), Act.press("Меньше 365 дней, или был перерыв (периоды не складываются)"),
      Act.press("Сегодня"), Act.press("Да"), Act.press("Ещё раз"))
    assertEquals(again.at.map(_.title), Some("Polska · Польща · Польша · Poland"))

  test("in Ukrainian: a child born in Poland whose mother holds the card — may apply, three questions only"):
    val p = played(Act.press("Українська"), Act.press("Посвідка CUKR (для громадян України зі статусом UKR)"),
      Act.press("Чи можу я подати заяву на посвідку CUKR?"),
      Act.press("Для дитини, народженої в Польщі після 23 лютого 2022 р."),
      Act.press("Так"), Act.press("Так"), Act.press("Сьогодні"), Act.press("Так"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Можна подавати"))

  test("in Polish: the card held, away for exactly six months — the page says the office's texts differ"):
    val p = played(Act.press("Polski"), Act.press("Karta pobytu CUKR (dla obywateli Ukrainy ze statusem UKR)"),
      Act.press("Mam kartę — na jak długo mogę wyjechać z Polski?"),
      Act.press("Dokładnie 6 miesięcy"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Mam kartę — na jak długo mogę wyjechać z Polski?"))

  test("in English: refused for a fee not paid, ten days ago — appeal, no refund, apply again"):
    val p = played(Act.press("English"), Act.press("The CUKR card (for Ukrainians with the UKR status)"),
      Act.press("I was refused the card"), Act.press("14 days or fewer"),
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
      Act.press("Да"), Act.press("Да"),
        Act.press("Есть документ о B1 или выше (сертификат, аттестат польской школы и т. п.)"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Можно подавать"))

  test("in Ukrainian: PESEL UKR — a Ukrainian who came 30 days ago or less, a passport, no exclusion — may get it"):
    val p = played(Act.press("Українська"), Act.press("PESEL і PESEL UKR"), Act.press("Як отримати PESEL UKR?"),
      Act.press("Громадянин України, жив(ла) в Україні до 24.02.2022, або член сім'ї"),
      Act.press("30 днів тому або менше"), Act.press("Так"), Act.press("Нічого з цього"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("PESEL UKR можна отримати"))

  test("in Russian: away for more than 30 days — the UKR status is lost"):
    val p = played(Act.press("Русский"), Act.press("PESEL и PESEL UKR"), Act.press("Сохранится ли мой статус UKR?"),
      Act.press("Больше 30 дней"), Act.press("Ничего из этого"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Статус UKR утрачен"))

  private def at(path: Vector[String], acts: Act*) =
    val went = collection.mutable.ArrayBuffer.empty[List[String]]
    (Play(steps(addressed[(Ui + Address), Ui, Unit](path.toList, went)(Assistant.main[(Ui + Address)](20261119))),
      acts*), went.toVector.map(_.toVector))

  test("every screen has its address: the language, the area, the topic, the answers given"):
    val (p, went) = at(Vector.empty, Act.press("Русский"), Act.press("PESEL и PESEL UKR"),
      Act.press("Сохранится ли мой статус UKR?"), Act.press("Больше 30 дней"))
    assertEquals(p.problems, Nil)
    assertEquals(went, Vector(Vector(), Vector("ru"), Vector("ru", "pesel"),
      Vector("ru", "pesel", "will-i-keep-the-ukr-status"), Vector("ru", "pesel", "will-i-keep-the-ukr-status",
        "away=long")))

  test("a link opened goes straight to its screen: the answer of the PESEL UKR conversation"):
    val link = Vector("uk", "pesel", "how-to-get-pesel-ukr", "who=ua", "crossed=in", "passport=yes", "excluded=none")
    val (p, went) = at(link)
    assertEquals(p.at.map(_.title), Some("PESEL UKR можна отримати"))
    assertEquals(went, Vector(link))

  test("a link to a question, and one with what no longer exists: the nearest screen that does"):
    val (q, _) = at(Vector("en", "work"))
    assertEquals(q.at.map(_.title), Some(Assistant.areas(20261119).find(_.id == "work").get.topics.head.name.en))
    val (old, went) = at(Vector("pl", "nothing-like-this", "x=y"))
    assertEquals(old.at.map(_.title), Some("Co Cię interesuje?"))
    assertEquals(went, Vector(Vector("pl")))

  test("topics' addresses differ within an area"):
    for a <- Assistant.areas(20261119) do
      assertEquals(a.topics.map(Assistant.slug).distinct.size, a.topics.size, a.id)

  test("in Ukrainian: came from Ukraine because of the war — four steps; 'more' opens PESEL UKR"):
    val p = played(Act.press("Українська"), Act.press("Я приїхав(ла) до Польщі: що робити?"),
      Act.press("Приїхав(ла) з України через війну (з 24.02.2022), або член сім'ї"))
    assertEquals(p.problems, Nil)
    assertEquals(p.at.map(_.title), Some("Ваші кроки"))
    val more = played(Act.press("Українська"), Act.press("Я приїхав(ла) до Польщі: що робити?"),
      Act.press("Приїхав(ла) з України через війну (з 24.02.2022), або член сім'ї"),
      Act.press("Детальніше: Як отримати PESEL UKR?"))
    assertEquals(more.at.map(_.title), Some("Як отримати PESEL UKR?"))

  test("every step's 'more' names a topic the page has"):
    for (_, r) <- okay.migrant.ArrivalTopic.reasons if r.see.nonEmpty do
      val (_, went) = at(Vector("en") ++ r.see)
      assert(went.headOption.exists(_.drop(1).startsWith(r.see)), s"${r.see} → $went")
