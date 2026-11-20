package okay.migrant

/** OUR TRANSLATIONS of the offices' sentences published in Polish alone, and of the sources' titles: shown to a
 * person in their language, marked unofficial, the Polish original beside. Where an office published a sentence in
 * the person's language, the office's own text is shown instead (`Outcome.quoted`). Keyed by the Polish text */
object Translations:
  private def t(pl: String, uk: String, ru: String, en: String): (String, Words) = pl -> Words(uk, ru, pl, en)

  val places: Map[String, Words] = Map(
    t("UdSC, «Informacja dotycząca procedury wydania karty pobytu CUKR»",
      "UdSC, «Інформація щодо процедури видачі посвідки CUKR»",
      "UdSC, «Информация о процедуре выдачи карты CUKR»",
      "UdSC, \"Information on the procedure for issuing the CUKR residence card\""),
    t("UdSC, «Karta pobytu CUKR – pytania i odpowiedzi», 8",
      "UdSC, «Посвідка CUKR — запитання і відповіді», 8",
      "UdSC, «Карта CUKR — вопросы и ответы», 8",
      "UdSC, \"The CUKR residence card — questions and answers\", 8"),
    t("UdSC, «Karta pobytu CUKR – pytania i odpowiedzi»",
      "UdSC, «Посвідка CUKR — запитання і відповіді»",
      "UdSC, «Карта CUKR — вопросы и ответы»",
      "UdSC, \"The CUKR residence card — questions and answers\""),
    t("UdSC, MOS: «Zezwolenie na pobyt stały»",
      "UdSC, MOS: «Дозвіл на постійне проживання»",
      "UdSC, MOS: «Разрешение на постоянное проживание»",
      "UdSC, MOS: \"Permanent residence permit\""),
    t("UdSC, MOS: «Baza pytań i odpowiedzi — zezwolenia bezterminowe»",
      "UdSC, MOS: «База запитань і відповідей — безстрокові дозволи»",
      "UdSC, MOS: «База вопросов и ответов — бессрочные разрешения»",
      "UdSC, MOS: \"Questions and answers — permanent permits\""),
    t("UdSC, MOS: «Zezwolenie na pobyt rezydenta długoterminowego UE»",
      "UdSC, MOS: «Дозвіл на проживання довгострокового резидента ЄС»",
      "UdSC, MOS: «Разрешение на пребывание долгосрочного резидента ЕС»",
      "UdSC, MOS: \"Long-term EU resident permit\""),
    t("UdSC, MOS: «Q&A: Zezwolenie na pobyt rezydenta długoterminowego UE»",
      "UdSC, MOS: «Запитання і відповіді: дозвіл довгострокового резидента ЄС»",
      "UdSC, MOS: «Вопросы и ответы: разрешение долгосрочного резидента ЕС»",
      "UdSC, MOS: \"Q&A: long-term EU resident permit\""),
    t("UdSC, MOS: «Zezwolenie na pobyt czasowy i pracę (zezwolenie jednolite)»",
      "UdSC, MOS: «Дозвіл на тимчасове проживання і роботу (єдиний дозвіл)»",
      "UdSC, MOS: «Разрешение на временное пребывание и работу (единое разрешение)»",
      "UdSC, MOS: \"Temporary residence and work permit (the single permit)\""),
    t("UdSC, MOS: «Baza pytań i odpowiedzi — zezwolenia: pobyt i praca»",
      "UdSC, MOS: «База запитань і відповідей — дозволи: проживання і робота»",
      "UdSC, MOS: «База вопросов и ответов — разрешения: пребывание и работа»",
      "UdSC, MOS: \"Questions and answers — permits: residence and work\""),
    t("MSWiA: «Najczęściej zadawane pytania i odpowiedzi» (status UKR)",
      "MSWiA: «Найчастіші запитання і відповіді» (статус UKR)",
      "MSWiA: «Часто задаваемые вопросы и ответы» (статус UKR)",
      "MSWiA: \"Frequently asked questions and answers\" (the UKR status)"),
    t("Urząd m.st. Warszawy, 19115: «Nadanie numeru PESEL na wniosek cudzoziemca…»",
      "Управа міста Варшави, 19115: «Присвоєння номера PESEL за заявою іноземця…»",
      "Управа города Варшавы, 19115: «Присвоение номера PESEL по заявлению иностранца…»",
      "City of Warsaw, 19115: \"Assigning a PESEL number on a foreigner's application…\""),
    t("Urząd m.st. Warszawy, 19115: «Udzielenie ochrony czasowej przez nadanie numeru PESEL ze statusem UKR»",
      "Управа міста Варшави, 19115: «Надання тимчасового захисту шляхом присвоєння номера PESEL зі статусом UKR»",
      "Управа города Варшавы, 19115: «Предоставление временной защиты путём присвоения номера PESEL со статусом UKR»",
      "City of Warsaw, 19115: \"Temporary protection by assigning a PESEL number with the UKR status\""))

  val quotes: Map[String, Words] = Map(
    t(("10 lat - na podstawie zgody na pobyt tolerowany udzielonej na podstawie art. 351 pkt 1 lub 3 ustawy o " +
      "cudzoziemcach;"),
      ("10 років — на підставі згоди на толероване перебування, наданої на підставі ст. 351 п. 1 або 3 закону про " +
        "іноземців;"),
      ("10 лет — на основании согласия на терпимое пребывание, выданного на основании ст. 351 п. 1 или 3 закона об " +
        "иностранцах;"),
      ("10 years — on the basis of consent for tolerated stay granted under Art. 351(1) or (3) of the Act on " +
        "Foreigners;")),
    t("1010 zł miesięcznie, jeśli cudzoziemiec gospodaruje samodzielnie i nie posiada osób na utrzymaniu;",
      "1010 зл на місяць, якщо іноземець веде господарство самостійно і не має осіб на утриманні;",
      "1010 злотых в месяц, если иностранец ведёт хозяйство самостоятельно и не имеет лиц на содержании;",
      "1010 zł a month if the foreigner keeps their own household and has no dependants;"),
    t("440 zł – w przypadku pracownika (za udzielenie zezwolenia na podstawie art. 114 ust. 1 ustawy o cudzoziemcach);",
      "440 зл — для працівника (за надання дозволу на підставі ст. 114 ч. 1 закону про іноземців);",
      "440 злотых — для работника (за выдачу разрешения на основании ст. 114 ч. 1 закона об иностранцах);",
      "440 zł — for an employee (for the permit granted under Art. 114(1) of the Act on Foreigners);"),
    t(("5 lat - w związku z nadaniem mu statusu uchodźcy, udzieleniem ochrony uzupełniającej lub zgody na pobyt ze " +
      "względów humanitarnych;"),
      ("5 років — у зв'язку з наданням йому статусу біженця, додаткового захисту або згоди на перебування з " +
        "гуманітарних міркувань;"),
      ("5 лет — в связи с предоставлением ему статуса беженца, дополнительной защиты или согласия на пребывание по " +
        "гуманитарным соображениям;"),
      ("5 years — in connection with being granted refugee status, subsidiary protection or consent for stay on " +
        "humanitarian grounds;")),
    t("Otrzymywane wynagrodzenie musi wynosić co najmniej 4806 zł brutto miesięcznie",
      "Отримувана винагорода має становити щонайменше 4806 зл брутто на місяць",
      "Получаемое вознаграждение должно составлять не менее 4806 злотых брутто в месяц",
      "The pay received must be at least 4806 zł gross a month"),
    t(("Z możliwości uzyskania tego zezwolenia zostali wyłączeni cudzoziemcy, którzy w chwili składania wniosku " +
      "przebywają w Polsce m.in.:"),
      "Можливості отримати цей дозвіл позбавлені іноземці, які на момент подання заяви перебувають у Польщі, зокрема:",
      ("Возможности получить это разрешение лишены иностранцы, которые на момент подачи заявления находятся в " +
        "Польше, в частности:"),
      "Excluded from this permit are foreigners who, when applying, stay in Poland, among others:"),
    t("cudzoziemiec ma ubezpieczenie zdrowotne;",
      "іноземець має медичне страхування;",
      "у иностранца есть медицинская страховка;",
      "the foreigner has health insurance;"),
    t("cudzoziemiec posiada potwierdzoną znajomość języka polskiego na poziomie najmniej B1;",
      "іноземець має підтверджене знання польської мови на рівні щонайменше B1;",
      "иностранец имеет подтверждённое знание польского языка на уровне не ниже B1;",
      "the foreigner has a confirmed knowledge of Polish at level B1 at least;"),
    t(("nie była jednorazowo dłuższa niż 6 miesięcy, a wszystkie przerwy razem nie przekroczyły 10 miesięcy w " +
      "okresie wymaganym do udzielenia zezwolenia"),
      ("не тривала одноразово довше 6 місяців, а всі перерви разом не перевищили 10 місяців за період, потрібний " +
        "для надання дозволу"),
      ("не длилось единовременно дольше 6 месяцев, а все перерывы вместе не превысили 10 месяцев за период, " +
        "требуемый для выдачи разрешения"),
      ("was not longer than 6 months at a time, and all breaks together did not exceed 10 months in the period " +
        "required for the permit")),
    t(("pozostawał w tym związku małżeńskim przez co najmniej 3 lata przed dniem złożenia wniosku o udzielenie mu " +
      "zezwolenia na pobyt stały"),
      "перебував у цьому шлюбі щонайменше 3 роки до дня подання заяви про надання йому дозволу на постійне проживання",
      "состоял в этом браке не менее 3 лет до дня подачи заявления о выдаче ему разрешения на постоянное проживание",
      "had been in this marriage for at least 3 years before the day of applying for the permanent residence permit"),
    t("pracownicy sezonowi, tj. wykonujący pracę uznawaną przez prawo za sezonową (np. w rolnictwie lub turystyce)",
      ("сезонні працівники, тобто ті, хто виконує роботу, яку закон визнає сезонною (напр., у сільському " +
        "господарстві чи туризмі)"),
      ("сезонные работники, т. е. выполняющие работу, которую закон признаёт сезонной (напр., в сельском хозяйстве " +
        "или туризме)"),
      "seasonal workers, i.e. doing work the law deems seasonal (e.g. in agriculture or tourism)"),
    t("4 czerwca 2025 r. miałeś aktualny PESEL UKR",
      "4 червня 2025 р. ви мали чинний PESEL UKR",
      "4 июня 2025 г. у вас был действующий PESEL UKR",
      "on 4 June 2025 you had a valid PESEL UKR"),
    t("Aby nie utracić karty pobytu CUKR nie możesz wyjechać z Polski jednorazowo na okres 6 miesięcy.",
      "Щоб не втратити посвідку CUKR, ви не можете виїхати з Польщі одноразово на 6 місяців.",
      "Чтобы не потерять карту CUKR, вы не можете выехать из Польши единовременно на 6 месяцев.",
      "Not to lose the CUKR card, you may not leave Poland for 6 months at a time."),
    t("Aby otrzymać kartę pobytu CUKR, musisz spełnić jednocześnie wszystkie cztery poniższe warunki",
      "Щоб отримати посвідку CUKR, ви маєте одночасно виконати всі чотири умови нижче",
      "Чтобы получить карту CUKR, вы должны одновременно выполнить все четыре условия ниже",
      "To get the CUKR card, you must meet all four conditions below at once"),
    t(("Będziesz musiał dokonać opłaty 200 zł, a jeśli kolejny raz utracisz kartę pobytu z własnej winy (np. kolejny " +
      "raz ją zgubisz), opłata wyniesie 300 zł."),
      ("Вам доведеться сплатити 200 зл, а якщо ви вдруге втратите посвідку з власної вини (напр., знову її " +
        "загубите), плата становитиме 300 зл."),
      ("Вам придётся заплатить 200 злотых, а если вы снова потеряете карту по своей вине (напр., опять её " +
        "потеряете), плата составит 300 злотых."),
      ("You will have to pay 200 zł, and if you lose the card again through your own fault (e.g. lose it once " +
        "more), the fee will be 300 zł.")),
    t(("Chodzi o listę sporządzaną przez starostę, która określa zawody, dla których nie wydaje się zezwoleń na " +
      "pracę z powodu trudnej sytuacji na lokalnym rynku pracy."),
      ("Ідеться про список, який складає староста: у ньому професії, для яких не видають дозволів на роботу через " +
        "складну ситуацію на місцевому ринку праці."),
      ("Речь о списке, который составляет староста: в нём профессии, для которых не выдают разрешений на работу " +
        "из-за трудной ситуации на местном рынке труда."),
      ("It is a list drawn up by the starost naming the occupations for which no work permits are issued because " +
        "of a difficult local labour market.")),
    t("Jeśli opuścisz Polskę na okres powyżej 6 miesięcy, utracisz zezwolenie na pobyt czasowy.",
      "Якщо ви покинете Польщу більш ніж на 6 місяців, ви втратите дозвіл на тимчасове проживання.",
      "Если вы покинете Польшу более чем на 6 месяцев, вы потеряете разрешение на временное пребывание.",
      "If you leave Poland for more than 6 months, you will lose the temporary residence permit."),
    t("Jeśli w rejestrach brakuje jakichś danych, system MOS zablokuje możliwość złożenia wniosku.",
      "Якщо в реєстрах бракує якихось даних, система MOS заблокує можливість подати заяву.",
      "Если в реестрах не хватает каких-то данных, система MOS заблокирует возможность подать заявление.",
      "If some data are missing in the registers, the MOS system will block the application."),
    t(("Posiadanie karty pobytu CUKR daje Ci prawo do prowadzenia działalności gospodarczej na takich samych " +
      "zasadach, jakie obowiązują obywateli polskich."),
      "Посвідка CUKR дає вам право вести підприємницьку діяльність на тих самих засадах, що й громадяни Польщі.",
      "Карта CUKR даёт вам право вести предпринимательскую деятельность на тех же условиях, что и граждане Польши.",
      "The CUKR card gives you the right to run a business on the same terms as Polish citizens."),
    t(("Samo złożenie wniosku o wydanie karty pobytu CUKR nie pozbawia Cię statusu UKR, zatem możesz wyjechać z " +
      "Polski, jednak nie dłużej niż na 30 dni."),
      ("Саме подання заяви на посвідку CUKR не позбавляє вас статусу UKR, тож ви можете виїхати з Польщі, але не " +
        "більш ніж на 30 днів."),
      ("Сама подача заявления на карту CUKR не лишает вас статуса UKR, поэтому вы можете выехать из Польши, но не " +
        "более чем на 30 дней."),
      ("Applying for the CUKR card does not by itself take away your UKR status, so you may leave Poland, but for " +
        "no more than 30 days.")),
    t("Status UKR jest anulowany w przypadku wyjazdu z Polski na okres powyżej 30 dni.",
      "Статус UKR анулюється в разі виїзду з Польщі більш ніж на 30 днів.",
      "Статус UKR аннулируется в случае выезда из Польши более чем на 30 дней.",
      "The UKR status is cancelled on leaving Poland for more than 30 days."),
    t(("Status ten umożliwia legalny pobyt w Polsce do 4 marca 2027 r. Daje uprawnienia do pracy bez dodatkowych " +
      "zezwoleń"),
      ("Цей статус дозволяє легально перебувати в Польщі до 4 березня 2027 р. Дає право працювати без додаткових " +
        "дозволів"),
      ("Этот статус позволяет легально находиться в Польше до 4 марта 2027 г. Даёт право работать без " +
        "дополнительных разрешений"),
      ("This status allows a legal stay in Poland until 4 March 2027. It gives the right to work with no further " +
        "permits")),
    t("Tak, z kartą pobytu CUKR masz pełny dostęp do polskiego rynku pracy.",
      "Так, з посвідкою CUKR ви маєте повний доступ до польського ринку праці.",
      "Да, с картой CUKR у вас полный доступ к польскому рынку труда.",
      "Yes, with the CUKR card you have full access to the Polish labour market."),
    t(("Twoja karta zostanie unieważniona i nie będziesz mógł jej odebrać. Nie będziesz mógł się ubiegać o zwrot " +
      "dokonanych opłat."),
      "Вашу посвідку буде анульовано, і ви не зможете її отримати. Ви не зможете вимагати повернення сплачених коштів.",
      "Ваша карта будет аннулирована, и вы не сможете её получить. Вы не сможете требовать возврата уплаченных сборов.",
      ("Your card will be cancelled and you will not be able to collect it. You will not be able to claim back the " +
        "fees paid.")),
    t("Wniosek o wydanie karty pobytu CUKR możesz złożyć jedynie w formie elektronicznej",
      "Заяву на посвідку CUKR можна подати лише в електронній формі",
      "Заявление на карту CUKR можно подать только в электронной форме",
      "The application for the CUKR card can only be filed electronically"),
    t("Zwrotu opłaty nie otrzymasz również, gdy postępowanie zostanie umorzone",
      "Плату вам не повернуть і тоді, коли провадження буде припинено",
      "Сбор вам не вернут и тогда, когда производство будет прекращено",
      "Nor will the fee be refunded when the proceedings are discontinued"),
    t("będę mógł ponownie złożyć wniosek o wydanie karty pobytu CUKR? Tak.",
      "чи зможу я знову подати заяву на посвідку CUKR? Так.",
      "смогу ли я снова подать заявление на карту CUKR? Да.",
      "will I be able to apply for the CUKR card again? Yes."),
    t(("masz prawo do złożenia w terminie 14 dni od dnia doręczenia decyzji odwołania do Szefa Urzędu do Spraw " +
      "Cudzoziemców"),
      ("ви маєте право протягом 14 днів від дня вручення рішення подати оскарження до Голови Управління у справах " +
        "іноземців"),
      "вы имеете право в течение 14 дней со дня вручения решения подать жалобу Главе Управления по делам иностранцев",
      ("you have the right to appeal to the Head of the Office for Foreigners within 14 days of the decision's " +
        "delivery")),
    t("matka dziecka przyjechała do Polski po 23 lutego 2022 r. i posiada kartę pobytu CUKR",
      "мати дитини приїхала до Польщі після 23 лютого 2022 р. і має посвідку CUKR",
      "мать ребёнка приехала в Польшу после 23 февраля 2022 г. и имеет карту CUKR",
      "the child's mother came to Poland after 23 February 2022 and holds the CUKR card"),
    t("możliwe będzie od 4 maja 2026 r.",
      "буде можливим з 4 травня 2026 р.",
      "будет возможно с 4 мая 2026 г.",
      "will be possible from 4 May 2026"),
    t("opuścisz Polskę na okres co najmniej 6 miesięcy",
      "ви покинете Польщу щонайменше на 6 місяців",
      "вы покинете Польшу как минимум на 6 месяцев",
      "you leave Poland for at least 6 months"),
    t("posiadałeś status UKR nieprzerwanie przez co najmniej 365 dni",
      "ви мали статус UKR безперервно щонайменше 365 днів",
      "у вас был статус UKR непрерывно не менее 365 дней",
      "you held the UKR status without a break for at least 365 days"),
    t("powinieneś to zrobić w ciągu 15 dni roboczych od dnia zmiany adresu",
      "ви маєте зробити це протягом 15 робочих днів від дня зміни адреси",
      "вы должны сделать это в течение 15 рабочих дней со дня смены адреса",
      "you should do it within 15 working days of the change of address"),
    t("w dniu wydania przez wojewodę karty pobytu CUKR będziesz miał aktualny numer PESEL UKR",
      "у день видачі воєводою посвідки CUKR ви матимете чинний номер PESEL UKR",
      "в день выдачи воеводой карты CUKR у вас будет действующий номер PESEL UKR",
      "on the day the voivode issues the CUKR card you will have a valid PESEL UKR number"),
    t("w dniu złożenia wniosku o wydanie karty pobytu CUKR musisz mieć aktualny PESEL UKR",
      "у день подання заяви на посвідку CUKR ви повинні мати чинний PESEL UKR",
      "в день подачи заявления на карту CUKR у вас должен быть действующий PESEL UKR",
      "on the day you apply for the CUKR card you must have a valid PESEL UKR"),
    t("wniosek o wydanie karty pobytu CUKR będziesz mógł złożyć do 04.03.2027 r.",
      "заяву на посвідку CUKR ви зможете подати до 04.03.2027 р.",
      "заявление на карту CUKR вы сможете подать до 04.03.2027 г.",
      "you can apply for the CUKR card until 04.03.2027"),
    t("Czy każde dziecko urodzone w Polsce automatycznie uzyskuje prawo stałego pobytu?",
      "Чи кожна дитина, народжена в Польщі, автоматично отримує право на постійне проживання?",
      "Получает ли каждый ребёнок, родившийся в Польше, автоматически право на постоянное проживание?",
      "Does every child born in Poland automatically get the right of permanent residence?"),
    t(("Jeśli jakiś urząd (np. Zakład Ubezpieczeń Społecznych czy urząd skarbowy) wymaga podania numeru PESEL – " +
      "powinien wskazać Ci również podstawę prawną."),
      "Якщо якась установа (напр., ZUS чи податкова) вимагає номер PESEL, вона має також вказати вам правову підставу.",
      ("Если какое-то учреждение (напр., ZUS или налоговая) требует номер PESEL, оно должно также указать вам " +
        "правовое основание."),
      ("If an office (e.g. the Social Insurance Institution, ZUS, or the tax office) requires a PESEL number, it " +
        "should also tell you the legal basis.")),
    t(("Jeśli jesteś cudzoziemcem i mieszkasz w Polsce, możesz się zameldować. Wtedy automatycznie otrzymasz numer " +
      "PESEL."),
      ("Якщо ви іноземець і живете в Польщі, ви можете зареєструвати місце проживання (замельдуватися). Тоді ви " +
        "автоматично отримаєте номер PESEL."),
      ("Если вы иностранец и живёте в Польше, вы можете зарегистрироваться по месту жительства (замельдоваться). " +
        "Тогда вы автоматически получите номер PESEL."),
      ("If you are a foreigner living in Poland, you can register your residence (zameldowanie). Then you get a " +
        "PESEL number automatically.")),
    t("Nie musisz płacić za złożenie tego wniosku.",
      "За подання цієї заяви платити не потрібно.",
      "За подачу этого заявления платить не нужно.",
      "You do not pay for filing this application."),
    t("Nie musisz płacić za złożenie wniosku o nadanie numeru PESEL w związku z konfliktem w Ukrainie.",
      "За подання заяви про присвоєння номера PESEL у зв'язку з конфліктом в Україні платити не потрібно.",
      "За подачу заявления о присвоении номера PESEL в связи с конфликтом в Украине платить не нужно.",
      "You do not pay for applying for a PESEL number in connection with the conflict in Ukraine."),
    t(("Numer PESEL ze statusem UKR możesz uzyskać, jeśli jesteś osobą, która przybyła legalnie do Polski w związku " +
      "z działaniami wojennymi prowadzonymi w Ukrainie od 24 lutego 2022 r."),
      ("Номер PESEL зі статусом UKR ви можете отримати, якщо ви особа, яка легально прибула до Польщі у зв'язку з " +
        "воєнними діями, що ведуться в Україні з 24 лютого 2022 р."),
      ("Номер PESEL со статусом UKR вы можете получить, если вы лицо, легально прибывшее в Польшу в связи с " +
        "военными действиями, которые ведутся в Украине с 24 февраля 2022 г."),
      ("You can get a PESEL number with the UKR status if you came to Poland legally because of the war waged in " +
        "Ukraine since 24 February 2022.")),
    t(("O udzielenie ochrony czasowej przez nadanie numeru PESEL ze statusem UKR nie możesz ubiegać się, jeśli " +
      "spełniasz którykolwiek z poniższych warunków:"),
      ("Ви не можете претендувати на тимчасовий захист через присвоєння номера PESEL зі статусом UKR, якщо " +
        "виконується будь-яка з таких умов:"),
      ("Вы не можете претендовать на временную защиту через присвоение номера PESEL со статусом UKR, если " +
        "выполняется любое из следующих условий:"),
      ("You may not apply for temporary protection by a PESEL number with the UKR status if any of the following " +
        "applies:")),
    t("Opłata skarbowa za udzielenie zezwolenia na pobyt rezydenta długoterminowego UE wynosi 640 zł.",
      "Гербовий збір за надання дозволу довгострокового резидента ЄС становить 640 зл.",
      "Гербовый сбор за выдачу разрешения долгосрочного резидента ЕС составляет 640 злотых.",
      "The stamp duty for the long-term EU resident permit is 640 zł."),
    t("Opłata skarbowa za udzielenie zezwolenia na pobyt stały wynosi 640 zł.",
      "Гербовий збір за надання дозволу на постійне проживання становить 640 зл.",
      "Гербовый сбор за выдачу разрешения на постоянное проживание составляет 640 злотых.",
      "The stamp duty for the permanent residence permit is 640 zł."),
    t(("Pobyt w Polsce uznaje się za nieprzerwany, jeśli żadna z przerw w pobycie nie przekroczyła 6 miesięcy a " +
      "łącznie przerwy w pobycie nie były dłuższe niż 10 miesięcy."),
      ("Перебування в Польщі вважається безперервним, якщо жодна з перерв не перевищила 6 місяців, а разом перерви " +
        "не були довшими за 10 місяців."),
      ("Пребывание в Польше считается непрерывным, если ни один из перерывов не превысил 6 месяцев, а вместе " +
        "перерывы были не дольше 10 месяцев."),
      ("A stay in Poland counts as unbroken if no break exceeded 6 months and all breaks together were not longer " +
        "than 10 months.")),
    t("Posiada ważny dokument podróży (paszport), który okaże przy zgłoszeniu w dowolnym urzędzie gminy.",
      "Має чинний проїзний документ (паспорт), який покаже під час звернення в будь-якому уряді гміни.",
      "Имеет действующий проездной документ (паспорт), который покажет при обращении в любой уряд гмины.",
      "Has a valid travel document (passport), shown when reporting at any municipal office."),
    t("Status UKR stracą również osoby, które:",
      "Статус UKR втратять також особи, які:",
      "Статус UKR потеряют также лица, которые:",
      "The UKR status will also be lost by those who:"),
    t("Udzielenie zezwolenia na pobyt stały na tej podstawie nie jest aktualnie możliwe",
      "Надання дозволу на постійне проживання на цій підставі наразі неможливе",
      "Выдача разрешения на постоянное проживание на этом основании сейчас невозможна",
      "A permanent residence permit on this ground is not possible at present"),
    t(("Ustawa o cudzoziemcach nie zawiera w zakresie zezwoleń na pobyt stały wymogu dotyczącego konieczności " +
      "posiadania przez cudzoziemca potwierdzonej znajomości języka polskiego"),
      ("Закон про іноземців щодо дозволів на постійне проживання не містить вимоги, щоб іноземець мав підтверджене " +
        "знання польської мови"),
      ("Закон об иностранцах в отношении разрешений на постоянное проживание не содержит требования, чтобы " +
        "иностранец имел подтверждённое знание польского языка"),
      ("For permanent residence permits the Act on Foreigners does not require the foreigner to have a confirmed " +
        "knowledge of Polish")),
    t("We wniosku wpisz faktyczną podstawę prawną, z której wynika obowiązek posiadania numeru PESEL.",
      "У заяві вкажи фактичну правову підставу, з якої випливає обов'язок мати номер PESEL.",
      "В заявлении укажи фактическое правовое основание, из которого следует обязанность иметь номер PESEL.",
      "In the application, write the actual legal basis that requires you to have a PESEL number."),
    t(("Wykonywanie pracy musi stanowić podstawę ubiegania się o zezwolenie na pobyt czasowy i pracę, a jednocześnie " +
      "musi uzasadniać pobyt w Polsce powyżej 3 miesięcy."),
      ("Виконання роботи має бути підставою для заяви на дозвіл на тимчасове проживання і роботу і водночас має " +
        "обґрунтовувати перебування в Польщі понад 3 місяці."),
      ("Выполнение работы должно быть основанием для заявления на разрешение на временное пребывание и работу и " +
        "одновременно обосновывать пребывание в Польше более 3 месяцев."),
      ("Work must be the ground of the application for the temporary residence and work permit and must also " +
        "justify a stay in Poland of more than 3 months.")),
    t(("Zezwolenia na pobyt rezydenta długoterminowego UE udziela się cudzoziemcowi, który przebywa na terytorium " +
      "Rzeczypospolitej Polskiej legalnie i nieprzerwanie co najmniej przez 5 lat bezpośrednio przed złożeniem " +
      "wniosku"),
      ("Дозвіл довгострокового резидента ЄС надається іноземцеві, який перебуває на території Республіки Польща " +
        "легально і безперервно щонайменше 5 років безпосередньо перед поданням заяви"),
      ("Разрешение долгосрочного резидента ЕС выдаётся иностранцу, который находится на территории Республики " +
        "Польша легально и непрерывно не менее 5 лет непосредственно перед подачей заявления"),
      ("The long-term EU resident permit is granted to a foreigner who has stayed in Poland legally and without a " +
        "break for at least 5 years right before applying")),
    t("Zezwolenie to udzielane jest na wniosek, w określonych prawem przypadkach.",
      "Цей дозвіл надається за заявою, у визначених законом випадках.",
      "Это разрешение выдаётся по заявлению, в предусмотренных законом случаях.",
      "This permit is granted on application, in the cases the law sets out."),
    t(("Zgłosi się w dowolnym urzędzie gminy w Polsce najpóźniej w ciągu 30 dni od przekroczenia granicy Polski. " +
      "Przekroczenie tego terminu uniemożliwia otrzymanie statusu UKR."),
      ("Звернеться до будь-якого уряду гміни в Польщі не пізніше ніж через 30 днів після перетину кордону Польщі. " +
        "Пропуск цього строку унеможливлює отримання статусу UKR."),
      ("Обратится в любой уряд гмины в Польше не позднее 30 дней после пересечения границы Польши. Пропуск этого " +
        "срока делает получение статуса UKR невозможным."),
      ("Reports at any municipal office in Poland within 30 days of crossing the Polish border. Missing this " +
        "deadline makes the UKR status impossible to get.")),
    t(("Znajomość języka polskiego jest potwierdzana w określony prawem sposób. Oznacza to, że nawet, jeśli świetnie " +
      "posługujesz się językiem polskim, musisz przedstawić na to konkretne, wymienione w ustawie dokumenty."),
      ("Знання польської мови підтверджується визначеним законом способом. Це означає, що навіть якщо ви чудово " +
        "володієте польською, ви маєте подати конкретні документи, названі в законі."),
      ("Знание польского языка подтверждается установленным законом способом. Это значит, что даже если вы отлично " +
        "владеете польским, вы должны представить конкретные документы, названные в законе."),
      ("Knowledge of Polish is confirmed in a way the law sets out. Even if you speak Polish very well, you must " +
        "present the specific documents the Act names.")),
    t(("bezpośrednio przed złożeniem tego wniosku przebywał nieprzerwanie w Polsce przez okres nie krótszy niż 2 " +
      "lata na podstawie zezwolenia na pobyt czasowy udzielonego w związku z pozostawaniem w związku małżeńskim z " +
      "tym obywatelem polskim"),
      ("безпосередньо перед поданням цієї заяви безперервно перебував у Польщі не менше 2 років на підставі " +
        "дозволу на тимчасове проживання, наданого у зв'язку з перебуванням у шлюбі з цим громадянином Польщі"),
      ("непосредственно перед подачей этого заявления непрерывно находился в Польше не менее 2 лет на основании " +
        "разрешения на временное пребывание, выданного в связи с браком с этим гражданином Польши"),
      ("right before applying, stayed in Poland without a break for at least 2 years on a temporary residence " +
        "permit granted for the marriage to that Polish citizen")),
    t("dokument podróży (paszport) w dowolnym urzędzie gminy do 31 sierpnia 2026 r.",
      "проїзний документ (паспорт) у будь-якому уряді гміни до 31 серпня 2026 р.",
      "проездной документ (паспорт) в любом уряде гмины до 31 августа 2026 г.",
      "a travel document (passport) at any municipal office by 31 August 2026"),
    t("jest niedopuszczalne. Wniosek należy złożyć w trakcie pobytu w Polsce.",
      "є неприпустимою. Заяву потрібно подати під час перебування в Польщі.",
      "недопустима. Заявление нужно подать во время пребывания в Польше.",
      "is not admissible. The application must be filed while staying in Poland."),
    t(("masz obowiązek powiadomić właściwego wojewodę, na piśmie, w terminie 15 dni, o utracie pracy u " +
      "któregokolwiek z podmiotów powierzających pracę, wymienionych w decyzji o udzieleniu zezwolenia na pobyt " +
      "czasowy i pracę."),
      ("ви зобов'язані письмово повідомити відповідного воєводу протягом 15 днів про втрату роботи в будь-якого з " +
        "роботодавців, названих у рішенні про дозвіл на тимчасове проживання і роботу."),
      ("вы обязаны письменно уведомить соответствующего воеводу в течение 15 дней о потере работы у любого из " +
        "работодателей, названных в решении о разрешении на временное пребывание и работу."),
      ("you must notify the competent voivode in writing within 15 days of losing work with any of the employers " +
        "named in the temporary residence and work permit decision.")),
    t(("nie niższe niż wynagrodzenie pracowników wykonujących, w tym samym wymiarze czasu pracy, pracę " +
      "porównywalnego rodzaju lub na porównywalnym stanowisku;"),
      ("не нижча за винагороду працівників, які в тому самому обсязі робочого часу виконують порівнянну роботу або " +
        "працюють на порівнянній посаді;"),
      ("не ниже вознаграждения работников, выполняющих при том же объёме рабочего времени сравнимую работу или на " +
        "сравнимой должности;"),
      "not lower than the pay of workers doing comparable work or in a comparable post with the same working hours;"),
    t("okazał aktualny tytuł prawny do zajmowania lokalu mieszkalnego, w którym będzie przebywał;",
      "показав чинну правову підставу для користування житлом, у якому проживатиме;",
      "предъявил действующее правовое основание для пользования жильём, в котором будет проживать;",
      "showed a current legal title to the home in which they will live;"),
    t("opuściły terytorium Polski na okres powyżej 30 dni,",
      "покинули територію Польщі більш ніж на 30 днів,",
      "покинули территорию Польши более чем на 30 дней,",
      "left Poland for more than 30 days,"),
    t(("otrzymałeś lub otrzymałaś ochronę czasową na terenie państwa członkowskiego Unii Europejskiej, innego niż " +
      "Polska, przyznaną z powodu działań wojennych prowadzonych w Ukrainie,"),
      ("ви отримали тимчасовий захист у державі — члені Європейського Союзу, іншій, ніж Польща, наданий через " +
        "воєнні дії в Україні,"),
      ("вы получили временную защиту в государстве — члене Европейского союза, кроме Польши, предоставленную из-за " +
        "военных действий в Украине,"),
      "you got temporary protection in an EU member state other than Poland, granted because of the war in Ukraine,"),
    t("pisma wojewody będą wysyłane na Twój poprzedni adres, jednak zostaną one uznane za skutecznie doręczone",
      "листи воєводи надсилатимуть на вашу попередню адресу, але вони вважатимуться належно врученими",
      "письма воеводы будут отправлять на ваш прежний адрес, но они будут считаться надлежаще врученными",
      "the voivode's letters will be sent to your previous address, yet they will count as duly delivered"),
    t("połowę okresu pobytu w Polsce na podstawie zezwolenia na pobyt czasowy wydanego w ww. celach",
      ("половину періоду перебування в Польщі на підставі дозволу на тимчасове проживання, виданого з указаною " +
        "вище метою"),
      ("половину периода пребывания в Польше на основании разрешения на временное пребывание, выданного в " +
        "указанных выше целях"),
      "half of the stay in Poland on a temporary residence permit issued for the purposes above"),
    t("pracownicy delegowani przez pracodawcę, który posiada siedzibę poza Polską",
      "працівники, відряджені роботодавцем, який має осідок за межами Польщі",
      "работники, командированные работодателем, который находится за пределами Польши",
      "workers posted by an employer based outside Poland"),
    t("prowadzący działalność gospodarczą w Polsce",
      "ті, хто веде підприємницьку діяльність у Польщі",
      "ведущие предпринимательскую деятельность в Польше",
      "those running a business in Poland"),
    t("przebywający w Polsce na podstawie wizy (typu D lub Schengen) wydanej przez konsula RP w celu:",
      "ті, хто перебуває в Польщі на підставі візи (типу D або шенгенської), виданої консулом Польщі з метою:",
      "находящиеся в Польше на основании визы (типа D или шенгенской), выданной консулом Польши с целью:",
      "those staying in Poland on a visa (type D or Schengen) issued by a Polish consul for:"),
    t("status UKR nadawany obywatelom Ukrainy i określonym członkom ich rodzin został przedłużony do 4 marca 2027 r.",
      "статус UKR, що надається громадянам України та визначеним членам їхніх сімей, продовжено до 4 березня 2027 р.",
      "статус UKR, предоставляемый гражданам Украины и определённым членам их семей, продлён до 4 марта 2027 г.",
      ("the UKR status given to Ukrainian citizens and certain members of their families has been extended to 4 " +
        "March 2027")),
    t(("w Polsce legalnie i nieprzerwanie przez co najmniej 5 lat bezpośrednio poprzedzających złożenie wniosku w " +
      "sprawie zezwolenia"),
      "у Польщі легально і безперервно щонайменше 5 років безпосередньо перед поданням заяви про дозвіл",
      "в Польше легально и непрерывно не менее 5 лет непосредственно перед подачей заявления о разрешении",
      "in Poland legally and without a break for at least 5 years right before applying for the permit"),
    t(("wjechałeś lub wjechałaś na terytorium Polski na podstawie zezwolenia na przekroczenie granicy w ramach " +
      "małego ruchu granicznego."),
      "ви в'їхали на територію Польщі за дозволом на перетин кордону в межах місцевого прикордонного руху.",
      "вы въехали на территорию Польши по разрешению на пересечение границы в рамках местного приграничного движения.",
      "you entered Poland on a local border traffic permit."),
    t(("wniosek o udzielenie zezwolenia na pobyt czasowy i pracę może zostać złożony za pośrednictwem MOS dopiero po " +
      "wypełnieniu załącznika nr 1 przez pracodawcę."),
      ("заяву на дозвіл на тимчасове проживання і роботу можна подати через MOS лише після того, як роботодавець " +
        "заповнить додаток № 1."),
      ("заявление на разрешение на временное пребывание и работу можно подать через MOS только после того, как " +
        "работодатель заполнит приложение № 1."),
      ("the application for the temporary residence and work permit can be filed through MOS only after the " +
        "employer fills in annex no. 1.")))
