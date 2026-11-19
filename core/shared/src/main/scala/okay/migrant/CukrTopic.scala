package okay.migrant

import okay.study.Model

/** THE CUKR CARD AS A TOPIC OF THE ASSISTANT: the questions that state a case (their answers are `Cukr`'s
 * canonical sentences), each verdict in plain words with the official sentence it rests on — quoted from
 * `books/pl/cukr/` in Polish and Ukrainian, as published — and what to remember beside a positive answer.
 * The plain wordings are ours and unofficial; the quotes are the office's */
object CukrTopic:
  private val sheet = "UdSC, «Informacja dotycząca procedury wydania karty pobytu CUKR»"
  private val faq = "UdSC, «Karta pobytu CUKR – pytania i odpowiedzi»"
  private def yes = Words("Так", "Да", "Tak", "Yes")
  private def no = Words("Ні", "Нет", "Nie", "No")
  private def yesNo(a: String, b: String) = Vector(Choice("yes", yes, a), Choice("no", no, b))

  /** dd.mm.yyyy or yyyy-mm-dd → the day as the number the model compares */
  def day(text: String): Option[Int] =
    val Dotted = """\s*(\d{1,2})\.(\d{1,2})\.(\d{4})\s*""".r
    val Iso = """\s*(\d{4})-(\d{1,2})-(\d{1,2})\s*""".r
    def valid(y: Int, m: Int, d: Int) = Option.when(m >= 1 && m <= 12 && d >= 1 && d <= 31)(y * 10000 + m * 100 + d)
    text match
      case Dotted(d, m, y) => valid(y.toInt, m.toInt, d.toInt)
      case Iso(y, m, d) => valid(y.toInt, m.toInt, d.toInt)
      case _ => None

  /** `today`: the day of the conversation, as a number (20261119) — the answer "today" states it */
  def topic(model: Model, today: Int): Topic = Topic(
    name = Words("Чи можу я подати заяву на посвідку CUKR?", "Могу ли я подать заявление на карту CUKR?",
      "Czy mogę złożyć wniosek o kartę pobytu CUKR?", "Can I apply for the CUKR card?"),
    yes = Some(Words("Можна подавати", "Можно подавать", "Można składać", "You may apply")),
    no = Some(Words("Зараз подати не вдасться", "Сейчас подать не получится", "Teraz nie można złożyć",
      "The application cannot be made now")),
    model = model,
    questions = Vector(
      Question("who", Words("Для кого посвідка?", "Для кого карта?", "Dla kogo jest karta?", "Who is the card for?"),
        Vector(
          Choice("came", Words("Для людини, яка приїхала до Польщі з України після 23 лютого 2022 р.",
            "Для человека, который приехал в Польшу из Украины после 23 февраля 2022 г.",
            "Dla osoby, która przyjechała do Polski z Ukrainy po 23 lutego 2022 r.",
            "For a person who came to Poland from Ukraine after 23 February 2022"), Cukr.came),
          Choice("born", Words("Для дитини, народженої в Польщі після 23 лютого 2022 р.",
            "Для ребёнка, родившегося в Польше после 23 февраля 2022 г.",
            "Dla dziecka urodzonego w Polsce po 23 lutego 2022 r.",
            "For a child born in Poland after 23 February 2022"), Cukr.born))),
      Question("pesel_today", Words("Чи є чинний PESEL UKR на день подання заяви?",
        "Есть ли действующий PESEL UKR на день подачи заявления?",
        "Czy w dniu złożenia wniosku jest aktualny PESEL UKR?",
        "Is PESEL UKR active on the day of the application?"), yesNo(Cukr.pesel(true), Cukr.pesel(false))),
      Question("pesel_june", Words("Чи був чинний PESEL UKR 4 червня 2025 р.?",
        "Был ли действующий PESEL UKR 4 июня 2025 г.?", "Czy 4 czerwca 2025 r. był aktualny PESEL UKR?",
        "Was PESEL UKR active on 4 June 2025?"), yesNo(Cukr.june(true), Cukr.june(false))),
      Question("days", Words("Скільки тривав статус UKR без перерви?", "Сколько длился статус UKR без перерыва?",
        "Jak długo trwał status UKR bez przerwy?", "How long has the UKR status lasted without a break?"),
        Vector(
          Choice("enough", Words("365 днів або більше, без перерви", "365 дней или больше, без перерыва",
            "365 dni lub więcej, bez przerwy", "365 days or more, with no break"), Cukr.days(365)),
          Choice("short", Words("Менше 365 днів, або була перерва (періоди не сумуються)",
            "Меньше 365 дней, или был перерыв (периоды не складываются)",
            "Mniej niż 365 dni albo była przerwa (okresy się nie sumują)",
            "Less than 365 days, or there was a break (periods do not add up)"), Cukr.days(364)))),
      Question("mother", Words("Чи має мати дитини посвідку CUKR?", "Есть ли у матери ребёнка карта CUKR?",
        "Czy matka dziecka posiada kartę pobytu CUKR?", "Does the child's mother hold a CUKR card?"),
        yesNo(Cukr.mother(true), Cukr.mother(false))),
      Question("date", Words("Коли подається заява? (сьогодні, або дата дд.мм.рррр)",
        "Когда подаётся заявление? (сегодня, или дата дд.мм.гггг)",
        "Kiedy składany jest wniosek? (dzisiaj albo data dd.mm.rrrr)",
        "When is the application made? (today, or a date dd.mm.yyyy)"),
        Vector(
          Choice("today", Words("Сьогодні", "Сегодня", "Dzisiaj", "Today"), Cukr.made(today)),
          Choice("before", Words("До 4 травня 2026 р.", "До 4 мая 2026 г.", "Przed 4 maja 2026 r.",
            "Before 4 May 2026"),
            Cukr.made(20260503)),
          Choice("within", Words("З 4 травня 2026 р. до 4 березня 2027 р.", "С 4 мая 2026 г. по 4 марта 2027 г.",
            "Od 4 maja 2026 r. do 4 marca 2027 r.", "From 4 May 2026 to 4 March 2027"), Cukr.made(20260601)),
          Choice("after", Words("Після 4 березня 2027 р.", "После 4 марта 2027 г.", "Po 4 marca 2027 r.",
            "After 4 March 2027"), Cukr.made(20270305))),
        typed = Some(text => day(text).map(Cukr.made))),
      Question("registers", Words(
        "Чи є в реєстрах усі потрібні дані (паспорт, відбитки пальців, зразок підпису)? Це показує MOS.",
        "Есть ли в реестрах все нужные данные (паспорт, отпечатки пальцев, образец подписи)? Это показывает MOS.",
        "Czy w rejestrach są wszystkie wymagane dane (paszport, odciski palców, wzór podpisu)? Pokazuje to MOS.",
        "Do the registers hold all the required data (passport, fingerprints, signature)? MOS shows it."),
        yesNo(Cukr.registers(true), Cukr.registers(false)))),
    reasons = Map(
      "eligible" -> Reason(Words("Заяву можна подати: умови, про які ми запитали, виконано.",
        "Заявление можно подать: условия, о которых мы спросили, выполнены.",
        "Wniosek można złożyć: warunki, o które zapytaliśmy, są spełnione.",
        "The application can be made: the conditions asked about are met."),
        Source(sheet, Map(
          "pl" -> "Aby otrzymać kartę pobytu CUKR, musisz spełnić jednocześnie wszystkie cztery poniższe warunki",
          "uk" -> "Щоб отримати посвідку на проживання CUKR, ви повинні одночасно виконати всі чотири наведені нижче умови")),
        good = true),
      "no_pesel_today" -> Reason(Words("Немає чинного PESEL UKR на день подання заяви.",
        "Нет действующего PESEL UKR на день подачи заявления.", "Brak aktualnego PESEL UKR w dniu złożenia wniosku.",
        "PESEL UKR is not active on the day of the application."),
        Source(sheet, Map(
          "pl" -> "w dniu złożenia wniosku o wydanie karty pobytu CUKR musisz mieć aktualny PESEL UKR",
          "uk" -> "у день подання заяви про видачу посвідки на проживання CUKR ви повинні мати чинний PESEL UKR"))),
      "no_pesel_june_2025" -> Reason(Words("4 червня 2025 р. не було чинного PESEL UKR.",
        "4 июня 2025 г. не было действующего PESEL UKR.", "4 czerwca 2025 r. nie było aktualnego PESEL UKR.",
        "PESEL UKR was not active on 4 June 2025."),
        Source(sheet, Map("pl" -> "4 czerwca 2025 r. miałeś aktualny PESEL UKR",
          "uk" -> "4 червня 2025 р. ви повинні були мати чинний PESEL UKR"))),
      "under_365_days" -> Reason(Words("Статус UKR не тривав безперервно щонайменше 365 днів.",
        "Статус UKR не длился непрерывно хотя бы 365 дней.",
        "Status UKR nie trwał nieprzerwanie co najmniej 365 dni.",
        "The UKR status has not lasted at least 365 days without a break."),
        Source(sheet, Map("pl" -> "posiadałeś status UKR nieprzerwanie przez co najmniej 365 dni",
          "uk" -> "ви мали статус UKR безперервно щонайменше протягом 365 днів"))),
      "mother_without_card" -> Reason(Words("Мати дитини, народженої в Польщі, не має посвідки CUKR.",
        "У матери ребёнка, родившегося в Польше, нет карты CUKR.",
        "Matka dziecka urodzonego w Polsce nie posiada karty pobytu CUKR.",
        "The mother of a child born in Poland does not hold a CUKR card."),
        Source(sheet, Map(
          "pl" -> "matka dziecka przyjechała do Polski po 23 lutego 2022 r. i posiada kartę pobytu CUKR",
          "uk" -> "мати дитини приїхала до Польщі після 23 лютого 2022 р. і має посвідку на проживання CUKR"))),
      "too_early" -> Reason(Words("Заяви приймаються з 4 травня 2026 р.", "Заявления принимаются с 4 мая 2026 г.",
        "Wnioski można składać od 4 maja 2026 r.", "Applications are accepted from 4 May 2026."),
        Source(sheet, Map("pl" -> "możliwe będzie od 4 maja 2026 r.", "uk" -> "буде можливим з 4 травня 2026 року"))),
      "too_late" -> Reason(Words("Заяву можна подати до 4 березня 2027 р.", "Заявление можно подать до 4 марта 2027 г.",
        "Wniosek można złożyć do 4 marca 2027 r.", "The application can be made until 4 March 2027."),
        Source(sheet, Map("pl" -> "wniosek o wydanie karty pobytu CUKR będziesz mógł złożyć do 04.03.2027 r.",
          "uk" -> "заяву про видачу посвідки на проживання CUKR можна подати до 04.03.2027 р."))),
      "registers_incomplete" -> Reason(Words("У реєстрах бракує даних: MOS не дасть подати заяву, доки їх не доповнено.",
        "В реестрах не хватает данных: MOS не даст подать заявление, пока их не дополнят.",
        "W rejestrach brakuje danych: MOS zablokuje złożenie wniosku, dopóki nie zostaną uzupełnione.",
        "The registers lack data: MOS blocks the application until they are completed."),
        Source(faq + ", 8", Map(
          "pl" -> "Jeśli w rejestrach brakuje jakichś danych, system MOS zablokuje możliwość złożenia wniosku.",
          "uk" -> "Якщо в реєстрах бракує будь-яких даних, система MOS заблокує можливість подання заяви.")))),
    remember = Vector(
      Reason(Words("PESEL UKR має бути чинним і в день видачі посвідки воєводою — цього зараз не перевірити.",
        "PESEL UKR должен быть действующим и в день выдачи карты воеводой — сейчас это проверить нельзя.",
        "PESEL UKR musi być aktualny także w dniu wydania karty przez wojewodę — tego nie da się teraz sprawdzić.",
        "PESEL UKR must still be active on the day the voivode issues the card — it cannot be checked now."),
        Source(sheet, Map(
          "pl" -> "w dniu wydania przez wojewodę karty pobytu CUKR będziesz miał aktualny numer PESEL UKR",
          "uk" -> "у день видачі воєводою посвідки на проживання CUKR ви повинні мати чинний номер PESEL UKR"))),
      Reason(Words("Заява подається лише в електронній формі, через портал MOS.",
        "Заявление подаётся только в электронной форме, через портал MOS.",
        "Wniosek składa się wyłącznie elektronicznie, przez portal MOS.",
        "The application is made only electronically, through the MOS portal."),
        Source(sheet, Map(
          "pl" -> "Wniosek o wydanie karty pobytu CUKR możesz złożyć jedynie w formie elektronicznej",
          "uk" -> "Заяву про видачу посвідки на проживання CUKR можна подати лише в електронній формі")))),
    book = Words("Джерело: матеріали Управління у справах іноземців (UdSC), отримані",
      "Источник: материалы Управления по делам иностранцев (UdSC), получены",
      "Źródło: materiały Urzędu do Spraw Cudzoziemców (UdSC), pobrane",
        "Source: the Office for Foreigners' (UdSC) materials, fetched"),
    fetched = "2026-11-19")
