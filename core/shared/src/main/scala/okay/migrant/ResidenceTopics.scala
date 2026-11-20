package okay.migrant

import okay.study.Model

/** THE TWO RESIDENCE PERMITS AS TOPICS OF THE ASSISTANT: the questions that state a case (their answers are the
 * canonical sentences of `Permanent` and `EuResident`), each verdict in plain words (ours, unofficial) with the
 * office's own sentences — quoted from `books/pl/permanent/` and `books/pl/eu-resident/` in Polish, Ukrainian and
 * Russian, as the MOS portal publishes them — and what to remember beside a positive answer */
object ResidenceTopics:
  private val fetched = "2026-11-20"
  private val book = Words("Джерело: портал MOS Управління у справах іноземців (UdSC), отримано",
    "Источник: портал MOS Управления по делам иностранцев (UdSC), получено",
    "Źródło: portal MOS Urzędu do Spraw Cudzoziemców (UdSC), pobrane",
    "Source: the Office for Foreigners' (UdSC) MOS portal, fetched")
  private val stalyPage = "UdSC, MOS: «Zezwolenie na pobyt stały»"
  private val stalyFaq = "UdSC, MOS: «Baza pytań i odpowiedzi — zezwolenia bezterminowe»"
  private val euPage = "UdSC, MOS: «Zezwolenie na pobyt rezydenta długoterminowego UE»"
  private val euFaq = "UdSC, MOS: «Q&A: Zezwolenie na pobyt rezydenta długoterminowego UE»"
  private def yes = Words("Так", "Да", "Tak", "Yes")
  private def no = Words("Ні", "Нет", "Nie", "No")
  private def yesNo(a: String, b: String) = Vector(Choice("yes", yes, a), Choice("no", no, b))
  private def q(page: String, pl: String, uk: String = "", ru: String = "") =
    Source(page, Map("pl" -> pl) ++ Option.when(uk.nonEmpty)("uk" -> uk) ++ Option.when(ru.nonEmpty)("ru" -> ru))

  // ———————————————————————————————— the permanent residence permit ————————————————————————————————

  private val permanentReasons: Map[String, Reason] = Map(
    "eligible" -> Reason(Words("За цією підставою дозвіл на постійне проживання можна отримати.",
      "По этому основанию разрешение на постоянное проживание можно получить.",
      "Na tej podstawie można uzyskać zezwolenie na pobyt stały.",
      "On this ground the permanent residence permit can be granted."),
      q(stalyPage, "Zezwolenie to udzielane jest na wniosek, w określonych prawem przypadkach."), good = true),
    "married_under_3_years" -> Reason(Words("Шлюб має тривати щонайменше 3 роки до подання заяви.",
      "Брак должен длиться не менее 3 лет до подачи заявления.",
      "Małżeństwo musi trwać co najmniej 3 lata przed złożeniem wniosku.",
      "The marriage must have lasted at least 3 years before applying."),
      q(stalyPage,
        "pozostawał w tym związku małżeńskim przez co najmniej 3 lata przed dniem złożenia wniosku o udzielenie mu zezwolenia na pobyt stały",
        "перебуває у шлюбі щонайменше 3 роки до дати подання заяви на отримання посвідки на постійне проживання",
        "состоял в браке не менее 3 лет до даты подачи заявления на получение постоянного вида на жительство")),
    "no_2_years_on_marriage_permit" -> Reason(Words(
      "Потрібно щонайменше 2 роки безперервного перебування в Польщі на тимчасовому дозволі у зв'язку з цим шлюбом — безпосередньо перед заявою.",
      "Нужно не менее 2 лет непрерывного пребывания в Польше на временном разрешении в связи с этим браком — непосредственно перед заявлением.",
      "Potrzeba co najmniej 2 lat nieprzerwanego pobytu w Polsce na zezwoleniu na pobyt czasowy w związku z tym małżeństwem — bezpośrednio przed wnioskiem.",
      "At least 2 years without a break in Poland on a temporary permit for this marriage are needed — right before applying."),
      q(stalyPage,
        "bezpośrednio przed złożeniem tego wniosku przebywał nieprzerwanie w Polsce przez okres nie krótszy niż 2 lata na podstawie zezwolenia na pobyt czasowy udzielonego w związku z pozostawaniem w związku małżeńskim z tym obywatelem polskim")),
    "protection_under_5_years" -> Reason(Words(
      "Потрібно щонайменше 5 років безперервного перебування у зв'язку зі статусом біженця, додатковим або гуманітарним захистом.",
      "Нужно не менее 5 лет непрерывного пребывания в связи со статусом беженца, дополнительной или гуманитарной защитой.",
      "Potrzeba co najmniej 5 lat nieprzerwanego pobytu w związku ze statusem uchodźcy, ochroną uzupełniającą lub zgodą humanitarną.",
      "At least 5 years without a break on refugee status, subsidiary protection or a humanitarian stay are needed."),
      q(stalyPage, "5 lat - w związku z nadaniem mu statusu uchodźcy, udzieleniem ochrony uzupełniającej lub zgody na pobyt ze względów humanitarnych;",
        "5 років - у зв'язку з отриманням статусу біженця, додаткового захисту або гуманітарного дозволу на проживання;",
        "5 лет - в связи с получением статуса беженца, дополнительной защиты или гуманитарного вида на жительство;"),
      Vector(q(stalyFaq, "Pobyt w Polsce uznaje się za nieprzerwany, jeśli żadna z przerw w pobycie nie przekroczyła 6 miesięcy a łącznie przerwy w pobycie nie były dłuższe niż 10 miesięcy."))),
    "tolerated_under_10_years" -> Reason(Words("Для дозволу на толероване перебування потрібно щонайменше 10 років.",
      "Для разрешения на терпимое пребывание нужно не менее 10 лет.",
      "Przy zgodzie na pobyt tolerowany potrzeba co najmniej 10 lat.",
      "On a tolerated stay at least 10 years are needed."),
      q(stalyPage, "10 lat - na podstawie zgody na pobyt tolerowany udzielonej na podstawie art. 351 pkt 1 lub 3 ustawy o cudzoziemcach;",
        "10 років - на підставі дозволу на толерантне перебування, виданого відповідно до ст. 351 ч. 1 або ч. 3 Закону про іноземців;",
        "10 лет - на основании разрешения на толерантное пребывание, выданного в соответствии со статьей 351(1) или (3) Закона об иностранцах;")),
    "child_born_at_another_time" -> Reason(Words(
      "Не кожна дитина, народжена в Польщі, отримує дозвіл: лише народжена після дозволу батька чи матері або під час їхнього тимчасового дозволу чи захисту.",
      "Не каждый ребёнок, родившийся в Польше, получает разрешение: только рождённый после разрешения родителя или во время его временного разрешения или защиты.",
      "Nie każde dziecko urodzone w Polsce: tylko urodzone po udzieleniu zezwolenia rodzicowi albo w czasie jego zezwolenia na pobyt czasowy lub ochrony.",
      "Not every child born in Poland: only one born after the parent's permit, or during the parent's temporary permit or protection."),
      q(stalyFaq, "Czy każde dziecko urodzone w Polsce automatycznie uzyskuje prawo stałego pobytu?")),
    "desired_occupation_not_possible_yet" -> Reason(Words(
      "Ця підстава поки не діє: розпорядження про бажані професії ще не видано.",
      "Это основание пока не действует: постановление о желательных профессиях ещё не издано.",
      "Ta podstawa jeszcze nie działa: rozporządzenie o zawodach pożądanych nie zostało wydane.",
      "This ground does not work yet: the regulation naming the desired occupations has not been issued."),
      q(stalyPage, "Udzielenie zezwolenia na pobyt stały na tej podstawie nie jest aktualnie możliwe")),
    "no_ground" -> Reason(Words(
      "Дозвіл на постійне проживання дають лише за підставами, переліченими в законі. Можливо, вам підходить дозвіл довгострокового резидента ЄС.",
      "Разрешение на постоянное проживание дают только по основаниям из закона. Возможно, вам подходит разрешение долгосрочного резидента ЕС.",
      "Zezwolenie na pobyt stały udziela się tylko w przypadkach wskazanych w ustawie. Może pasuje Ci zezwolenie rezydenta długoterminowego UE.",
      "The permanent residence permit is granted only on the law's grounds. The long-term EU resident permit may suit you."),
      q(stalyPage, "Zezwolenie to udzielane jest na wniosek, w określonych prawem przypadkach.")))

  def permanent(model: Model): Topic = Topic(
    name = Words("Дозвіл на постійне проживання (pobyt stały)", "Разрешение на постоянное проживание (pobyt stały)",
      "Zezwolenie na pobyt stały", "Permanent residence permit (pobyt stały)"),
    model = model,
    questions = Vector(
      Question("ground", Words("Що з цього про вас?", "Что из этого про вас?", "Co dotyczy Ciebie?",
        "Which of these is you?"), Vector(
        Choice("spouse", Words("Я у шлюбі з громадянином / громадянкою Польщі", "Я в браке с гражданином / гражданкой Польши",
          "Jestem w związku małżeńskim z obywatelem / obywatelką Polski", "I am married to a Polish citizen"),
          Permanent.spouse),
        Choice("karta", Words("Маю чинну Карту поляка і хочу оселитися в Польщі", "У меня действующая Карта поляка, хочу поселиться в Польше",
          "Mam ważną Kartę Polaka i chcę osiedlić się w Polsce", "I hold a valid Karta Polaka and will settle in Poland"),
          Permanent.kartaPolaka),
        Choice("origin", Words("Я польського походження і хочу оселитися в Польщі", "Я польского происхождения, хочу поселиться в Польше",
          "Jestem osobą polskiego pochodzenia i chcę osiedlić się w Polsce", "I am of Polish origin and will settle in Poland"),
          Permanent.polishOrigin),
        Choice("child", Words("Я дитина громадянина Польщі, під його батьківською опікою", "Я ребёнок гражданина Польши, под его родительской опекой",
          "Jestem dzieckiem obywatela polskiego, pod jego władzą rodzicielską", "I am a child of a Polish citizen in their parental custody"),
          Permanent.polishChild),
        Choice("settled", Words("Я дитина іноземця, який має в Польщі постійний дозвіл або статус резидента ЄС",
          "Я ребёнок иностранца с постоянным разрешением или статусом резидента ЕС в Польше",
          "Jestem dzieckiem cudzoziemca z zezwoleniem na pobyt stały lub rezydenta UE w Polsce",
          "I am a child of a foreigner holding a permanent or EU long-term resident permit in Poland"), Permanent.settledChild),
        Choice("protection", Words("Маю статус біженця, додатковий або гуманітарний захист", "У меня статус беженца, дополнительная или гуманитарная защита",
          "Mam status uchodźcy, ochronę uzupełniającą lub zgodę humanitarną", "I have refugee status, subsidiary protection or a humanitarian stay"),
          Permanent.protection),
        Choice("tolerated", Words("Маю дозвіл на толероване перебування", "У меня разрешение на терпимое пребывание",
          "Mam zgodę na pobyt tolerowany", "I have a tolerated stay"), Permanent.tolerated),
        Choice("asylum", Words("Мені надано притулок у Польщі", "Мне предоставлено убежище в Польше",
          "Udzielono mi azylu w Polsce", "I was granted asylum in Poland"), Permanent.asylum),
        Choice("desired", Words("Працюю за професією, бажаною для польської економіки", "Работаю по профессии, желательной для польской экономики",
          "Pracuję w zawodzie pożądanym dla polskiej gospodarki", "I work in an occupation desired for the Polish economy"),
          Permanent.desired),
        Choice("none", Words("Нічого з цього", "Ничего из этого", "Nic z tego", "None of these"), Permanent.none))),
      Question("married", Words("Скільки років триває шлюб?", "Сколько лет длится брак?", "Ile lat trwa małżeństwo?",
        "How long has the marriage lasted?"), Vector(
        Choice("long", Words("3 роки або більше", "3 года или больше", "3 lata lub dłużej", "3 years or more"), Permanent.married(3)),
        Choice("short", Words("Менше 3 років", "Меньше 3 лет", "Krócej niż 3 lata", "Less than 3 years"), Permanent.married(2)))),
      Question("two", Words(
        "Чи перебували ви безпосередньо перед заявою щонайменше 2 роки без перерви в Польщі на тимчасовому дозволі у зв'язку з цим шлюбом?",
        "Были ли вы непосредственно перед заявлением не менее 2 лет без перерыва в Польше на временном разрешении в связи с этим браком?",
        "Czy bezpośrednio przed wnioskiem przebywałeś(-aś) nieprzerwanie co najmniej 2 lata w Polsce na zezwoleniu na pobyt czasowy w związku z tym małżeństwem?",
        "Right before applying, have you stayed in Poland for at least 2 years without a break on a temporary permit for this marriage?"),
        yesNo(Permanent.twoYears(true), Permanent.twoYears(false))),
      Question("years", Words(
        "Скільки років ви перебуваєте в Польщі на цій підставі без перерви? (перерва — не більше 6 місяців за раз і 10 загалом)",
        "Сколько лет вы находитесь в Польше на этом основании без перерыва? (перерыв — не больше 6 месяцев за раз и 10 всего)",
        "Ile lat przebywasz w Polsce na tej podstawie nieprzerwanie? (przerwa — najwyżej 6 miesięcy jednorazowo i 10 łącznie)",
        "For how many years have you stayed in Poland on this ground without a break? (a break: at most 6 months at once, 10 in all)"),
        Vector(
          Choice("ten", Words("10 років або більше", "10 лет или больше", "10 lat lub dłużej", "10 years or more"), Permanent.years(10)),
          Choice("five", Words("Від 5 до 10 років", "От 5 до 10 лет", "Od 5 do 10 lat", "From 5 to 10 years"), Permanent.years(5)),
          Choice("less", Words("Менше 5 років", "Меньше 5 лет", "Krócej niż 5 lat", "Less than 5 years"), Permanent.years(4)))),
      Question("born", Words("Коли народилася дитина?", "Когда родился ребёнок?", "Kiedy urodziło się dziecko?",
        "When was the child born?"), Vector(
        Choice("time", Words(
          "Після того, як батько чи мати отримали цей дозвіл, або коли вони мали тимчасовий дозвіл чи захист у Польщі",
          "После того как родитель получил это разрешение, или когда у него было временное разрешение или защита в Польше",
          "Po udzieleniu rodzicowi tego zezwolenia albo gdy rodzic miał w Polsce zezwolenie na pobyt czasowy lub ochronę",
          "After the parent got this permit, or while the parent held a temporary permit or protection in Poland"),
          Permanent.bornInTime(true)),
        Choice("other", Words("В інший час", "В другое время", "W innym czasie", "At another time"), Permanent.bornInTime(false))))),
    reasons = permanentReasons,
    remember = Vector(
      Reason(Words("Плата — 640 зл. (власники Карти поляка та особи з притулком звільнені). Знання польської мови не вимагається.",
        "Плата — 640 зл. (владельцы Карты поляка и получившие убежище освобождены). Знание польского языка не требуется.",
        "Opłata — 640 zł (zwolnieni posiadacze Karty Polaka i azylanci). Znajomość języka polskiego nie jest wymagana.",
        "The fee is 640 zł (Karta Polaka holders and those granted asylum are exempt). Polish is not required."),
        q(stalyFaq, "Opłata skarbowa za udzielenie zezwolenia na pobyt stały wynosi 640 zł."),
        Vector(q(stalyFaq, "Ustawa o cudzoziemcach nie zawiera w zakresie zezwoleń na pobyt stały wymogu dotyczącego konieczności posiadania przez cudzoziemca potwierdzonej znajomości języka polskiego")))),
    book = book, fetched = fetched,
    yes = Some(Words("Можна подавати", "Можно подавать", "Można składać", "You may apply")),
    no = Some(Words("За цією підставою — ні", "По этому основанию — нет", "Na tej podstawie — nie", "Not on this ground")))

  // ———————————————————————————————— the long-term EU resident permit ————————————————————————————————

  private val euReasons: Map[String, Reason] = Map(
    "eligible" -> Reason(Words("Умови, про які ми запитали, виконано: можна подавати заяву.",
      "Условия, о которых мы спросили, выполнены: можно подавать заявление.",
      "Warunki, o które zapytaliśmy, są spełnione: można złożyć wniosek.",
      "The conditions asked about are met: the application can be made."),
      q(euFaq, "Zezwolenia na pobyt rezydenta długoterminowego UE udziela się cudzoziemcowi, który przebywa na terytorium Rzeczypospolitej Polskiej legalnie i nieprzerwanie co najmniej przez 5 lat bezpośrednio przed złożeniem wniosku"),
      good = true),
    "current_basis_excluded" -> Reason(Words(
      "На момент заяви ви перебуваєте на підставі, яка виключає цей дозвіл (навчання, тимчасовий захист, гуманітарне чи толероване перебування, процедура захисту тощо).",
      "На момент заявления вы находитесь на основании, исключающем это разрешение (учёба, временная защита, гуманитарное или терпимое пребывание, процедура защиты и т. п.).",
      "W chwili składania wniosku przebywasz na podstawie, która wyłącza to zezwolenie (nauka, ochrona czasowa, pobyt humanitarny lub tolerowany, procedura ochrony itd.).",
      "At the time of applying you stay on a basis that excludes this permit (studies, temporary protection, a humanitarian or tolerated stay, a protection procedure, etc.)."),
      q(euPage, "Z możliwości uzyskania tego zezwolenia zostali wyłączeni cudzoziemcy, którzy w chwili składania wniosku przebywają w Polsce m.in.:",
        "Від можливості отримання цього дозволу виключені іноземці, які на момент подання заяви перебувають у Польщі, зокрема:",
        "Из возможности получения данного разрешения исключены иностранцы, которые на момент подачи заявления находятся в Польше, в частности:")),
    "stay_too_short" -> Reason(Words("Зарахований законний безперервний строк перебування менший за 5 років.",
      "Засчитанный законный непрерывный срок пребывания меньше 5 лет.",
      "Zaliczany legalny i nieprzerwany pobyt jest krótszy niż 5 lat.",
      "The counted legal stay without a break is shorter than 5 years."),
      q(euPage, "w Polsce legalnie i nieprzerwanie przez co najmniej 5 lat bezpośrednio poprzedzających złożenie wniosku w sprawie zezwolenia"),
      Vector(q(euPage, "połowę okresu pobytu w Polsce na podstawie zezwolenia na pobyt czasowy wydanego w ww. celach"))),
    "absence_too_long" -> Reason(Words("Одна з відсутностей була довшою за 6 місяців — це перериває перебування.",
      "Одно из отсутствий было дольше 6 месяцев — это прерывает пребывание.",
      "Jedna z nieobecności trwała dłużej niż 6 miesięcy — to przerywa pobyt.",
      "One absence lasted longer than 6 months — it breaks the stay."),
      q(euPage, "nie była jednorazowo dłuższa niż 6 miesięcy, a wszystkie przerwy razem nie przekroczyły 10 miesięcy w okresie wymaganym do udzielenia zezwolenia",
        "не перевищував 6 місяців за один раз, а всі перерви разом не перевищували 10 місяців у необхідний для надання дозволу період",
        "не превышал 6 месяцев единовременно, а все перерывы вместе не превышали 10 месяцев в течение периода, необходимого для выдачи разрешения")),
    "absences_too_long" -> Reason(Words("Усі відсутності разом перевищили 10 місяців — це перериває перебування.",
      "Все отсутствия вместе превысили 10 месяцев — это прерывает пребывание.",
      "Wszystkie nieobecności razem przekroczyły 10 miesięcy — to przerywa pobyt.",
      "All absences together exceeded 10 months — it breaks the stay."),
      q(euPage, "nie była jednorazowo dłuższa niż 6 miesięcy, a wszystkie przerwy razem nie przekroczyły 10 miesięcy w okresie wymaganym do udzielenia zezwolenia",
        "не перевищував 6 місяців за один раз, а всі перерви разом не перевищували 10 місяців у необхідний для надання дозволу період",
        "не превышал 6 месяцев единовременно, а все перерывы вместе не превышали 10 месяцев в течение периода, необходимого для выдачи разрешения")),
    "income_not_shown" -> Reason(Words(
      "Потрібен стабільний і регулярний дохід за останні 3 роки (2 — для Блакитної карти): з 2025 р. понад 1010 зл. на місяць для однієї особи, 823 зл. на кожного члена сім'ї.",
      "Нужен стабильный и регулярный доход за последние 3 года (2 — для Голубой карты): с 2025 г. более 1010 зл. в месяц на одного, 823 зл. на каждого члена семьи.",
      "Potrzebny jest stabilny i regularny dochód przez ostatnie 3 lata (2 — przy Niebieskiej Karcie): od 2025 r. ponad 1010 zł miesięcznie dla osoby samotnej, 823 zł na członka rodziny.",
      "A stable and regular income for the last 3 years is needed (2 with a Blue Card): from 2025 over 1010 zł a month alone, 823 zł per family member."),
      q(euPage, "1010 zł miesięcznie, jeśli cudzoziemiec gospodaruje samodzielnie i nie posiada osób na utrzymaniu;",
        "1010 злотих на місяць, якщо іноземець господарює самостійно і не має утриманців;",
        "1010 злотых в месяц, если иностранец ведет самостоятельное хозяйство и не имеет иждивенцев;")),
    "no_insurance" -> Reason(Words("Потрібне медичне страхування.", "Нужна медицинская страховка.",
      "Wymagane jest ubezpieczenie zdrowotne.", "Health insurance is required."),
      q(euPage, "cudzoziemiec ma ubezpieczenie zdrowotne;", "іноземець має медичне страхування;",
        "иностранный гражданин имеет медицинскую страховку;")),
    "no_polish_b1" -> Reason(Words(
      "Потрібне підтверджене документом знання польської на рівні B1 (якщо на момент заяви вам уже є 16) — добре говорити недостатньо.",
      "Нужно подтверждённое документом знание польского на уровне B1 (если на момент заявления вам уже 16) — хорошо говорить недостаточно.",
      "Potrzebna jest znajomość polskiego na poziomie B1 potwierdzona dokumentem (jeśli masz ukończone 16 lat) — dobra znajomość nie wystarczy.",
      "Polish at B1 confirmed by a listed document is needed (if you are 16 or older) — speaking it well is not enough."),
      q(euPage, "Znajomość języka polskiego jest potwierdzana w określony prawem sposób. Oznacza to, że nawet, jeśli świetnie posługujesz się językiem polskim, musisz przedstawić na to konkretne, wymienione w ustawie dokumenty."),
      Vector(q(euPage, "cudzoziemiec posiada potwierdzoną znajomość języka polskiego na poziomie najmniej B1;",
        "іноземець має підтверджене знання польської мови на рівні щонайменше B1;",
        "иностранный гражданин имеет подтверждённое знание польского языка не ниже уровня B1;"))))

  def euResident(model: Model): Topic = Topic(
    name = Words("Дозвіл довгострокового резидента ЄС", "Разрешение долгосрочного резидента ЕС",
      "Zezwolenie na pobyt rezydenta długoterminowego UE", "Long-term EU resident permit"),
    model = model,
    questions = Vector(
      Question("basis", Words("На якій підставі ви перебуваєте в Польщі зараз?", "На каком основании вы находитесь в Польше сейчас?",
        "Na jakiej podstawie przebywasz teraz w Polsce?", "On what basis are you in Poland now?"), Vector(
        Choice("allowed", Words("Робота, бізнес, сім'я, постійний дозвіл або інший дозвіл на тимчасове перебування",
          "Работа, бизнес, семья, постоянное разрешение или другое временное разрешение",
          "Praca, działalność, rodzina, pobyt stały lub inne zezwolenie na pobyt czasowy",
          "Work, a business, family, permanent residence or another temporary permit"), EuResident.basis(false)),
        Choice("excluded", Words(
          "Навчання, тимчасовий захист (UKR), гуманітарне чи толероване перебування, притулок, процедура захисту, внутрішньокорпоративне переведення",
          "Учёба, временная защита (UKR), гуманитарное или терпимое пребывание, убежище, процедура защиты, внутрикорпоративный перевод",
          "Nauka, ochrona czasowa (UKR), pobyt humanitarny lub tolerowany, azyl, procedura ochrony, przeniesienie wewnątrz przedsiębiorstwa",
          "Studies, temporary protection (UKR), a humanitarian or tolerated stay, asylum, a protection procedure, an intra-corporate transfer"),
          EuResident.basis(true)))),
      Question("counted", Words(
        "Скільки років законного перебування зараховується? Повністю — робота, сім'я, бізнес; навпіл — студентські роки; не зараховуються — тимчасовий захист, школа, туристична віза.",
        "Сколько лет законного пребывания засчитывается? Полностью — работа, семья, бизнес; наполовину — студенческие годы; не засчитываются — временная защита, школа, туристическая виза.",
        "Ile lat legalnego pobytu się zalicza? W całości — praca, rodzina, działalność; w połowie — studia; wcale — ochrona czasowa, szkoła, wiza turystyczna.",
        "How many years of legal stay count? Fully — work, family, a business; half — university studies; not at all — temporary protection, school, a tourist visa."),
        Vector(
          Choice("enough", Words("5 років або більше", "5 лет или больше", "5 lat lub więcej", "5 years or more"), EuResident.counted(60)),
          Choice("short", Words("Менше 5 років", "Меньше 5 лет", "Mniej niż 5 lat", "Less than 5 years"), EuResident.counted(59)))),
      Question("longest", Words(
        "Найдовша відсутність у Польщі за ці 5 років? (не рахуйте відрядження від польського роботодавця, практику польського вишу, вимушену відсутність до 6 місяців)",
        "Самое долгое отсутствие в Польше за эти 5 лет? (не считайте командировки от польского работодателя, практику польского вуза, вынужденное отсутствие до 6 месяцев)",
        "Najdłuższa nieobecność w Polsce w tych 5 latach? (nie licz delegacji polskiego pracodawcy, praktyk polskiej uczelni, wymuszonej nieobecności do 6 miesięcy)",
        "The longest absence from Poland in these 5 years? (do not count work abroad for a Polish employer, a Polish university's practice, a forced absence of up to 6 months)"),
        Vector(
          Choice("ok", Words("6 місяців або менше", "6 месяцев или меньше", "6 miesięcy lub mniej", "6 months or less"), EuResident.longest(6)),
          Choice("long", Words("Більше 6 місяців", "Больше 6 месяцев", "Więcej niż 6 miesięcy", "More than 6 months"), EuResident.longest(7)))),
      Question("together", Words("Усі відсутності разом за ці 5 років?", "Все отсутствия вместе за эти 5 лет?",
        "Wszystkie nieobecności razem w tych 5 latach?", "All absences together in these 5 years?"), Vector(
        Choice("ok", Words("10 місяців або менше", "10 месяцев или меньше", "10 miesięcy lub mniej", "10 months or less"), EuResident.together(10)),
        Choice("long", Words("Більше 10 місяців", "Больше 10 месяцев", "Więcej niż 10 miesięcy", "More than 10 months"), EuResident.together(11)))),
      Question("income", Words(
        "Чи мали ви стабільний і регулярний дохід останні 3 роки — з 2025 р. понад 1010 зл. на місяць для однієї особи або 823 зл. на кожного члена сім'ї?",
        "Был ли у вас стабильный и регулярный доход последние 3 года — с 2025 г. более 1010 зл. в месяц на одного или 823 зл. на каждого члена семьи?",
        "Czy przez ostatnie 3 lata masz stabilny i regularny dochód — od 2025 r. ponad 1010 zł miesięcznie dla osoby samotnej lub 823 zł na członka rodziny?",
        "Have you had a stable and regular income for the last 3 years — from 2025 over 1010 zł a month alone, or 823 zł per family member?"),
        yesNo(EuResident.income(true), EuResident.income(false))),
      Question("insured", Words("Чи є у вас медичне страхування?", "Есть ли у вас медицинская страховка?",
        "Czy masz ubezpieczenie zdrowotne?", "Do you have health insurance?"),
        yesNo(EuResident.insured(true), EuResident.insured(false))),
      Question("polish", Words("Знання польської мови", "Знание польского языка", "Znajomość języka polskiego",
        "Polish"), Vector(
        Choice("doc", Words("Маю документ про B1 або вище (сертифікат, атестат польської школи тощо)",
          "Есть документ о B1 или выше (сертификат, аттестат польской школы и т. п.)",
          "Mam dokument potwierdzający B1 lub wyżej (certyfikat, świadectwo polskiej szkoły itd.)",
          "I have a document for B1 or higher (a certificate, a Polish school's diploma, etc.)"), EuResident.polish(true)),
        Choice("nodoc", Words("Документа немає", "Документа нет", "Nie mam dokumentu", "I have no such document"),
          EuResident.polish(false)),
        Choice("young", Words("Мені менше 16 років", "Мне меньше 16 лет", "Mam mniej niż 16 lat", "I am under 16"),
          EuResident.young)))),
    reasons = euReasons,
    remember = Vector(
      Reason(Words("Плата — 640 зл.; заява подається через MOS; на візиті потрібен документ про право на житло.",
        "Плата — 640 зл.; заявление подаётся через MOS; на визите нужен документ о праве на жильё.",
        "Opłata — 640 zł; wniosek składa się przez MOS; na wizycie trzeba okazać tytuł prawny do lokalu.",
        "The fee is 640 zł; the application goes through MOS; at the visit a title to your home is needed."),
        q(euFaq, "Opłata skarbowa za udzielenie zezwolenia na pobyt rezydenta długoterminowego UE wynosi 640 zł."),
        Vector(q(euPage, "okazał aktualny tytuł prawny do zajmowania lokalu mieszkalnego, w którym będzie przebywał;")))),
    book = book, fetched = fetched,
    yes = Some(Words("Можна подавати", "Можно подавать", "Można składać", "You may apply")),
    no = Some(Words("Зараз — ні", "Сейчас — нет", "Teraz — nie", "Not now")))

  // ———————————————————————————————— the single permit: temporary residence and work ————————————————————————————————

  private val workPage = "UdSC, MOS: «Zezwolenie na pobyt czasowy i pracę (zezwolenie jednolite)»"
  private val workFaq = "UdSC, MOS: «Baza pytań i odpowiedzi — zezwolenia: pobyt i praca»"

  private val workReasons: Map[String, Reason] = Map(
    "eligible" -> Reason(Words("Умови, про які ми запитали, виконано: заяву можна подати.",
      "Условия, о которых мы спросили, выполнены: заявление можно подать.",
      "Warunki, o które zapytaliśmy, są spełnione: można złożyć wniosek.",
      "The conditions asked about are met: the application can be made."),
      q(workPage, "Wykonywanie pracy musi stanowić podstawę ubiegania się o zezwolenie na pobyt czasowy i pracę, a jednocześnie musi uzasadniać pobyt w Polsce powyżej 3 miesięcy."),
      good = true),
    "apply_in_poland" -> Reason(Words("Заяву можна подати лише під час законного перебування в Польщі, не з-за кордону.",
      "Заявление можно подать только во время законного пребывания в Польше, не из-за границы.",
      "Wniosek można złożyć tylko w trakcie legalnego pobytu w Polsce, nie z zagranicy.",
      "The application can be made only during a legal stay in Poland, not from abroad."),
      q(workPage, "jest niedopuszczalne. Wniosek należy złożyć w trakcie pobytu w Polsce.")),
    "basis_excluded" -> Reason(Words(
      "Ваша нинішня підстава перебування виключає цей дозвіл (туристична чи гостьова віза, студентська віза, транзит, лікування тощо).",
      "Ваше нынешнее основание пребывания исключает это разрешение (туристическая или гостевая виза, студенческая виза, транзит, лечение и т. п.).",
      "Twoja obecna podstawa pobytu wyklucza to zezwolenie (wiza turystyczna lub w odwiedziny, wiza studencka, tranzyt, leczenie itd.).",
      "Your present basis of stay excludes this permit (a tourist or family-visit visa, a student visa, transit, medical treatment, etc.)."),
      q(workPage, "przebywający w Polsce na podstawie wizy (typu D lub Schengen) wydanej przez konsula RP w celu:")),
    "another_permit_seasonal" -> Reason(Words("Для сезонної роботи — інший дозвіл (на сезонну роботу).",
      "Для сезонной работы — другое разрешение (на сезонную работу).",
      "Do pracy sezonowej służy inne zezwolenie (na pobyt ze względu na pracę sezonową).",
      "Seasonal work has another permit (residence for seasonal work)."),
      q(workPage, "pracownicy sezonowi, tj. wykonujący pracę uznawaną przez prawo za sezonową (np. w rolnictwie lub turystyce)",
        "сезонні працівники, тобто ті, які виконують роботу, визнану законом сезонною (наприклад, у сільському господарстві або туризмі)",
        "сезонные работники, т.е. те, кто выполняет работу, признанную законом сезонной (например, в сельском хозяйстве или туризме)")),
    "another_permit_posted" -> Reason(Words("Працівникам, відрядженим іноземним роботодавцем, — інший дозвіл.",
      "Работникам, командированным иностранным работодателем, — другое разрешение.",
      "Pracownicy delegowani przez pracodawcę zagranicznego — inne zezwolenie.",
      "Workers posted by a foreign employer need another permit."),
      q(workPage, "pracownicy delegowani przez pracodawcę, który posiada siedzibę poza Polską")),
    "another_permit_business" -> Reason(Words("Для власної справи — інший дозвіл (на ведення господарської діяльності).",
      "Для своего дела — другое разрешение (на ведение предпринимательской деятельности).",
      "Do prowadzenia działalności gospodarczej służy inne zezwolenie.",
      "Running one's own business has another permit."),
      q(workPage, "prowadzący działalność gospodarczą w Polsce")),
    "stay_not_over_3_months" -> Reason(Words("Мета має виправдовувати перебування понад 3 місяці.",
      "Цель должна оправдывать пребывание более 3 месяцев.",
      "Cel musi uzasadniać pobyt w Polsce powyżej 3 miesięcy.",
      "The purpose must justify a stay of more than 3 months."),
      q(workPage, "Wykonywanie pracy musi stanowić podstawę ubiegania się o zezwolenie na pobyt czasowy i pracę, a jednocześnie musi uzasadniać pobyt w Polsce powyżej 3 miesięcy.")),
    "pay_under_minimum" -> Reason(Words(
      "Заробіток має бути щонайменше 4806 зл. брутто на місяць (мінімальна зарплата 2026), незалежно від ставки; від кількох роботодавців — разом.",
      "Заработок должен быть не меньше 4806 зл. брутто в месяц (минимальная зарплата 2026), независимо от ставки; от нескольких работодателей — вместе.",
      "Wynagrodzenie musi wynosić co najmniej 4806 zł brutto miesięcznie (płaca minimalna 2026) bez względu na wymiar etatu; od kilku pracodawców — łącznie.",
      "The pay must be at least 4806 zł gross a month (the 2026 minimum), whatever the hours; from several employers, together."),
      q(workFaq, "Otrzymywane wynagrodzenie musi wynosić co najmniej 4806 zł brutto miesięcznie",
        "Отримана винагорода повинна становити щонайменше 4806 злотих брутто на місяць",
        "Получаемое вознаграждение должно составлять не менее 4806 злотых брутто в месяц")),
    "pay_under_comparable" -> Reason(Words("Заробіток не може бути нижчим, ніж у працівників на порівнянній роботі.",
      "Заработок не может быть ниже, чем у работников на сравнимой работе.",
      "Wynagrodzenie nie może być niższe niż pracowników na porównywalnym stanowisku.",
      "The pay may not be lower than that of workers in comparable work."),
      q(workPage, "nie niższe niż wynagrodzenie pracowników wykonujących, w tym samym wymiarze czasu pracy, pracę porównywalnego rodzaju lub na porównywalnym stanowisku;")),
    "no_insurance" -> Reason(Words("Потрібне медичне страхування (за трудовим договором чи договором доручення воно є за законом).",
      "Нужна медицинская страховка (по трудовому договору или договору поручения она есть по закону).",
      "Wymagane jest ubezpieczenie zdrowotne (przy umowie o pracę lub zleceniu jest z mocy prawa).",
      "Health insurance is required (an employment or a mandate contract gives it by law)."),
      q(workPage, "cudzoziemiec ma ubezpieczenie zdrowotne;", "іноземець має медичну страховку;",
        "у иностранца есть медицинская страховка;")),
    "occupation_closed" -> Reason(Words(
      "Професія у списку старости, для якої в повіті не видають дозволів (крім продовження тієї ж роботи в того ж роботодавця і громадян Туреччини).",
      "Профессия в списке старосты, по которой в повяте не выдают разрешений (кроме продолжения той же работы у того же работодателя и граждан Турции).",
      "Zawód jest na liście starosty, na który nie wydaje się zezwoleń (poza kontynuacją tej samej pracy u tego samego pracodawcy i obywatelami Turcji).",
      "The occupation is on the starost's list closed to permits (except the same job continued with the same employer, and Turkish citizens)."),
      q(workPage, "Chodzi o listę sporządzaną przez starostę, która określa zawody, dla których nie wydaje się zezwoleń na pracę z powodu trudnej sytuacji na lokalnym rynku pracy.",
        "Йдеться про список, складений керівником району, в якому визначені професії, для яких не видаються дозволи на працевлаштування, через складну ситуацію на місцевому ринку праці;")))

  def work(model: Model): Topic = Topic(
    name = Words("Дозвіл на перебування і роботу", "Разрешение на пребывание и работу",
      "Zezwolenie na pobyt czasowy i pracę", "Temporary residence and work permit"),
    model = model,
    questions = Vector(
      Question("where", Words("Де ви зараз і на якій підставі?", "Где вы сейчас и на каком основании?",
        "Gdzie jesteś teraz i na jakiej podstawie?", "Where are you now, and on what basis?"), Vector(
        Choice("allowed", Words("У Польщі законно: безвіз, віза для роботи, інший дозвіл на перебування",
          "В Польше законно: безвиз, рабочая виза, другое разрешение на пребывание",
          "W Polsce legalnie: ruch bezwizowy, wiza w celu pracy, inne zezwolenie na pobyt",
          "In Poland legally: visa-free, a work visa, another residence permit"), Work.allowed),
        Choice("excluded", Words("У Польщі за туристичною, гостьовою, студентською візою, транзитом, на лікуванні",
          "В Польше по туристической, гостевой, студенческой визе, транзитом, на лечении",
          "W Polsce na wizie turystycznej, w odwiedziny, studenckiej, tranzytowej, na leczenie",
          "In Poland on a tourist, family-visit, student, transit or medical visa"), Work.excluded),
        Choice("abroad", Words("Поза Польщею", "За пределами Польши", "Poza Polską", "Outside Poland"), Work.abroad))),
      Question("purpose", Words("Яка робота?", "Какая работа?", "Jaka praca?", "What work?"), Vector(
        Choice("work", Words("Робота в польського роботодавця довше ніж 3 місяці", "Работа у польского работодателя дольше 3 месяцев",
          "Praca u pracodawcy w Polsce dłużej niż 3 miesiące", "Work for an employer in Poland for more than 3 months"),
          Work.work),
        Choice("seasonal", Words("Сезонна (сільське господарство, туризм)", "Сезонная (сельское хозяйство, туризм)",
          "Sezonowa (rolnictwo, turystyka)", "Seasonal (farming, tourism)"), Work.seasonal),
        Choice("posted", Words("Мене відряджає іноземний роботодавець", "Меня командирует иностранный работодатель",
          "Deleguje mnie pracodawca zagraniczny", "A foreign employer posts me"), Work.posted),
        Choice("business", Words("Власна справа", "Своё дело", "Własna działalność", "My own business"), Work.business),
        Choice("short", Words("До 3 місяців", "До 3 месяцев", "Do 3 miesięcy", "3 months or less"), Work.short))),
      Question("pay", Words(
        "Скільки ви отримуватимете брутто на місяць від усіх роботодавців разом? (мінімум 2026 — 4806 зл., незалежно від ставки)",
        "Сколько вы будете получать брутто в месяц от всех работодателей вместе? (минимум 2026 — 4806 зл., независимо от ставки)",
        "Ile będziesz zarabiać brutto miesięcznie od wszystkich pracodawców razem? (minimum 2026 — 4806 zł, bez względu na etat)",
        "How much gross a month from all employers together? (the 2026 minimum is 4806 zł, whatever the hours)"), Vector(
        Choice("enough", Words("4806 зл. або більше", "4806 зл. или больше", "4806 zł lub więcej", "4806 zł or more"), Work.pay(4806)),
        Choice("less", Words("Менше 4806 зл.", "Меньше 4806 зл.", "Mniej niż 4806 zł", "Less than 4806 zł"), Work.pay(4805)))),
      Question("comparable", Words("Чи заробіток не нижчий, ніж у колег на такій самій роботі?",
        "Заработок не ниже, чем у коллег на такой же работе?", "Czy wynagrodzenie nie jest niższe niż kolegów na takim samym stanowisku?",
        "Is the pay not lower than that of colleagues in the same work?"),
        Vector(Choice("yes", yes, Work.comparable(true)), Choice("no", no, Work.comparable(false)))),
      Question("insured", Words("Чи матимете медичне страхування? (за трудовим договором чи дорученням — так)",
        "Будет ли медицинская страховка? (по трудовому договору или поручению — да)",
        "Czy będziesz mieć ubezpieczenie zdrowotne? (przy umowie o pracę lub zleceniu — tak)",
        "Will you have health insurance? (an employment or mandate contract — yes)"),
        Vector(Choice("yes", yes, Work.insured(true)), Choice("no", no, Work.insured(false)))),
      Question("listed", Words("Чи є ваша професія у списку старости, закритому для дозволів? (його публікує повіт)",
        "Есть ли ваша профессия в списке старосты, закрытом для разрешений? (его публикует повят)",
        "Czy Twój zawód jest na liście starosty zamkniętej dla zezwoleń? (publikuje ją powiat)",
        "Is your occupation on the starost's list closed to permits? (the district publishes it)"), Vector(
        Choice("no", Words("Ні", "Нет", "Nie", "No"), Work.notListed),
        Choice("excepted", Words("Так, але я продовжую ту саму роботу в того самого роботодавця або я громадянин Туреччини",
          "Да, но я продолжаю ту же работу у того же работодателя или я гражданин Турции",
          "Tak, ale kontynuuję tę samą pracę u tego samego pracodawcy albo jestem obywatelem Turcji",
          "Yes, but I continue the same job with the same employer, or I am a Turkish citizen"), Work.excepted),
        Choice("yes", Words("Так", "Да", "Tak", "Yes"), Work.listed)))),
    reasons = workReasons,
    remember = Vector(
      Reason(Words("Плата — 440 зл. за дозвіл і 100 зл. за карту; роботодавець заповнює і підписує додаток № 1 у MOS.",
        "Плата — 440 зл. за разрешение и 100 зл. за карту; работодатель заполняет и подписывает приложение № 1 в MOS.",
        "Opłata — 440 zł za zezwolenie i 100 zł za kartę; pracodawca wypełnia i podpisuje załącznik nr 1 w MOS.",
        "The fee is 440 zł for the permit and 100 zł for the card; the employer fills in and signs annex no. 1 in MOS."),
        q(workFaq, "440 zł – w przypadku pracownika (za udzielenie zezwolenia na podstawie art. 114 ust. 1 ustawy o cudzoziemcach);",
          "440 PLN – у випадку працівника (за видачу дозволу на підставі ст. 114 ч. 1 Закону про іноземців);",
          "440 PLN – в случае работника (за выдачу разрешения на основании ст. 114 ч. 1 Закона об иностранцах);"),
        Vector(q(workFaq, "wniosek o udzielenie zezwolenia na pobyt czasowy i pracę może zostać złożony za pośrednictwem MOS dopiero po wypełnieniu załącznika nr 1 przez pracodawcę."))),
      Reason(Words("Втрату роботи в роботодавця з рішення треба письмово повідомити воєводі протягом 15 днів; зміна роботодавця з рішення — це зміна дозволу.",
        "О потере работы у работодателя из решения нужно письменно сообщить воеводе в течение 15 дней; смена работодателя из решения — это изменение разрешения.",
        "Utratę pracy u pracodawcy z decyzji zgłoś wojewodzie na piśmie w ciągu 15 dni; zmiana pracodawcy z decyzji wymaga zmiany zezwolenia.",
        "Losing the job with an employer named in the decision must be told to the voivode in writing within 15 days; changing that employer needs a change of the permit."),
        q(workFaq, "masz obowiązek powiadomić właściwego wojewodę, na piśmie, w terminie 15 dni, o utracie pracy u któregokolwiek z podmiotów powierzających pracę, wymienionych w decyzji o udzieleniu zezwolenia na pobyt czasowy i pracę."))),
    book = book, fetched = fetched,
    yes = Some(Words("Можна подавати", "Можно подавать", "Można składać", "You may apply")),
    no = Some(Words("Зараз — ні", "Сейчас — нет", "Teraz — nie", "Not now")))
