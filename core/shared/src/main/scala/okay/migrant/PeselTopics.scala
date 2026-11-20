package okay.migrant

import okay.study.Model

/** PESEL AND PESEL UKR AS TOPICS OF THE ASSISTANT: getting PESEL UKR, keeping the UKR status, an ordinary PESEL —
 * three situations over one model (`Pesel`). The quotes are the offices' (`books/pl/pesel/`: MSWiA, the City of
 * Warsaw; the UKR status's meaning from `books/pl/cukr/`), the plain wordings ours and unofficial */
object PeselTopics:
  private val fetched = "2026-11-20"
  private val book = Words("Джерело: MSWiA та Управа міста Варшави (портал 19115), отримано",
    "Источник: MSWiA и городское управление Варшавы (портал 19115), получено",
    "Źródło: MSWiA oraz Urząd m.st. Warszawy (portal 19115), pobrane",
    "Source: the Ministry of the Interior (MSWiA) and the City of Warsaw (19115), fetched")
  private val mswia = "MSWiA: «Najczęściej zadawane pytania i odpowiedzi» (status UKR)"
  private val ukrPage =
    "Urząd m.st. Warszawy, 19115: «Udzielenie ochrony czasowej przez nadanie numeru PESEL ze statusem UKR»"
  private val peselPage = "Urząd m.st. Warszawy, 19115: «Nadanie numeru PESEL na wniosek cudzoziemca…»"
  private val cukrFaq = "UdSC, «Karta pobytu CUKR – pytania i odpowiedzi»"
  private def q(page: String, pl: String, uk: String = "") =
    Source(page, Map("pl" -> pl) ++ Option.when(uk.nonEmpty)("uk" -> uk))
  private def yes = Words("Так", "Да", "Tak", "Yes")
  private def no = Words("Ні", "Нет", "Nie", "No")

  /** what the UKR status is, beside a plain PESEL: said with every answer of these topics */
  private val difference = Reason(Words(
    ("PESEL UKR — це не просто номер: він підтверджує тимчасовий захист — законне перебування до 4 березня 2027 р. " +
      "і роботу без дозволу. Звичайний PESEL — лише ідентифікаційний номер, підстави перебування він не дає."),
    ("PESEL UKR — не просто номер: он подтверждает временную защиту — законное пребывание до 4 марта 2027 г. и " +
      "работу без разрешения. Обычный PESEL — только идентификационный номер, основания для пребывания он не даёт."),
    ("PESEL UKR to nie tylko numer: potwierdza ochronę czasową — legalny pobyt do 4 marca 2027 r. i pracę bez " +
      "zezwolenia. Zwykły PESEL to tylko numer identyfikacyjny, nie daje podstawy pobytu."),
    ("PESEL UKR is more than a number: it confirms temporary protection — a legal stay until 4 March 2027 and work " +
      "with no permit. An ordinary PESEL is only an identification number; it is no basis of stay.")),
    q(cukrFaq,
      ("Status ten umożliwia legalny pobyt w Polsce do 4 marca 2027 r. Daje uprawnienia do pracy bez dodatkowych " +
      "zezwoleń"),
      "дозволяє легально перебувати в Польщі до 4 березня 2027 р."))

  private val reasons: Map[String, Reason] = Map(
    "may_get_pesel_ukr" -> Reason(Words(
      ("PESEL UKR можна отримати: безкоштовно, особисто, у будь-якому урядi гміни; потрібні паспорт і кольорове " +
        "фото 35×45 мм, беруть відбитки пальців (з 6 років) і зразок підпису (з 12)."),
      ("PESEL UKR можно получить: бесплатно, лично, в любом уряде гмины; нужны паспорт и цветное фото 35×45 мм, " +
        "берут отпечатки пальцев (с 6 лет) и образец подписи (с 12)."),
      ("PESEL UKR można uzyskać: bezpłatnie, osobiście, w dowolnym urzędzie gminy; potrzebny paszport i kolorowe " +
        "zdjęcie 35×45 mm, pobierane są odciski palców (od 6 lat) i wzór podpisu (od 12)."),
      ("PESEL UKR can be got: free, in person, at any municipal office; a passport and a colour photo 35×45 mm are " +
        "needed, fingerprints are taken (from 6) and a signature (from 12).")),
      q(ukrPage,
        ("Numer PESEL ze statusem UKR możesz uzyskać, jeśli jesteś osobą, która przybyła legalnie do Polski w " +
        "związku z działaniami wojennymi prowadzonymi w Ukrainie od 24 lutego 2022 r.")),
      Vector(q(ukrPage,
        "Nie musisz płacić za złożenie wniosku o nadanie numeru PESEL w związku z konfliktem w Ukrainie.")),
          good = true),
    "not_entitled" -> Reason(Words(
      ("PESEL UKR надається лише громадянам України, які жили в Україні до 24.02.2022, особам із захистом або " +
        "законним перебуванням в Україні до того дня, і членам їхніх сімей."),
      ("PESEL UKR выдаётся только гражданам Украины, жившим в Украине до 24.02.2022, лицам с защитой или законным " +
        "пребыванием в Украине до этого дня и членам их семей."),
      ("PESEL UKR przysługuje tylko obywatelom Ukrainy mieszkającym w Ukrainie przed 24.02.2022, osobom z ochroną " +
        "lub legalnym pobytem w Ukrainie przed tym dniem i członkom ich rodzin."),
      ("PESEL UKR is only for Ukrainians who lived in Ukraine before 24 Feb 2022, those with protection or a legal " +
        "stay in Ukraine before that day, and their families.")),
      q(ukrPage,
        ("Numer PESEL ze statusem UKR możesz uzyskać, jeśli jesteś osobą, która przybyła legalnie do Polski w " +
        "związku z działaniami wojennymi prowadzonymi w Ukrainie od 24 lutego 2022 r."))),
    "too_late_30_days" -> Reason(Words(
      "Звернутися до гміни треба не пізніше ніж за 30 днів від перетину кордону — цей строк минув.",
      "Обратиться в гмину нужно не позже 30 дней от пересечения границы — этот срок прошёл.",
      "Trzeba zgłosić się do gminy najpóźniej w ciągu 30 dni od przekroczenia granicy — ten termin minął.",
      "One must come to a municipal office within 30 days of crossing the border — the time has passed."),
      q(mswia,
        ("Zgłosi się w dowolnym urzędzie gminy w Polsce najpóźniej w ciągu 30 dni od przekroczenia granicy Polski. " +
        "Przekroczenie tego terminu uniemożliwia otrzymanie statusu UKR."))),
    "needs_passport" -> Reason(Words(
      "Потрібен чинний паспорт (проїзний документ), його показують у гміні.",
      "Нужен действующий паспорт (проездной документ), его показывают в гмине.",
      "Potrzebny jest ważny paszport (dokument podróży), okazywany w gminie.",
      "A valid passport (travel document) is needed, shown at the office."),
      q(mswia, "Posiada ważny dokument podróży (paszport), który okaże przy zgłoszeniu w dowolnym urzędzie gminy.")),
    "excluded_other_permit" -> Reason(Words(
      "Хто має в Польщі дозвіл на перебування або захист, PESEL UKR не отримує.",
      "Кто имеет в Польше разрешение на пребывание или защиту, PESEL UKR не получает.",
      "Kto ma w Polsce zezwolenie na pobyt lub ochronę, nie otrzymuje PESEL UKR.",
      "One holding a residence permit or protection in Poland does not get PESEL UKR."),
      q(ukrPage,
        ("O udzielenie ochrony czasowej przez nadanie numeru PESEL ze statusem UKR nie możesz ubiegać się, jeśli " +
        "spełniasz którykolwiek z poniższych warunków:"))),
    "excluded_protection_application" -> Reason(Words(
      "Хто подав заяву на міжнародний захист у Польщі, PESEL UKR не отримує.",
      "Кто подал заявление о международной защите в Польше, PESEL UKR не получает.",
      "Kto złożył w Polsce wniosek o ochronę międzynarodową, nie otrzymuje PESEL UKR.",
      "One who applied for international protection in Poland does not get PESEL UKR."),
      q(ukrPage,
        ("O udzielenie ochrony czasowej przez nadanie numeru PESEL ze statusem UKR nie możesz ubiegać się, jeśli " +
        "spełniasz którykolwiek z poniższych warunków:"))),
    "excluded_other_eu_state" -> Reason(Words(
      "Хто отримав тимчасовий захист в іншій державі ЄС, PESEL UKR не отримує.",
      "Кто получил временную защиту в другой стране ЕС, PESEL UKR не получает.",
      "Kto otrzymał ochronę czasową w innym państwie UE, nie otrzymuje PESEL UKR.",
      "One who got temporary protection in another EU state does not get PESEL UKR."),
      q(ukrPage,
        ("otrzymałeś lub otrzymałaś ochronę czasową na terenie państwa członkowskiego Unii Europejskiej, innego niż " +
        "Polska, przyznaną z powodu działań wojennych prowadzonych w Ukrainie,"))),
    "excluded_border_traffic" -> Reason(Words(
      "Хто в'їхав за дозволом на місцевий прикордонний рух, PESEL UKR не отримує.",
      "Кто въехал по разрешению на местное приграничное движение, PESEL UKR не получает.",
      "Kto wjechał na podstawie zezwolenia na mały ruch graniczny, nie otrzymuje PESEL UKR.",
      "One who entered on a local border traffic permit does not get PESEL UKR."),
      q(ukrPage,
        ("wjechałeś lub wjechałaś na terytorium Polski na podstawie zezwolenia na przekroczenie granicy w ramach " +
        "małego ruchu granicznego."))),
    "status_kept" -> Reason(Words(
      "Статус UKR зберігається (до 4 березня 2027 р.): виїзд до 30 днів дозволений.",
      "Статус UKR сохраняется (до 4 марта 2027 г.): выезд до 30 дней разрешён.",
      "Status UKR zostaje (do 4 marca 2027 r.): wyjazd do 30 dni jest dozwolony.",
      "The UKR status is kept (until 4 March 2027): an absence of up to 30 days is allowed."),
      q(mswia,
        ("status UKR nadawany obywatelom Ukrainy i określonym członkom ich rodzin został przedłużony do 4 marca 2027 " +
        "r.")), good = true),
    "lost_left_over_30_days" -> Reason(Words(
      "Виїзд із Польщі довше ніж на 30 днів позбавляє статусу UKR.",
      "Выезд из Польши дольше чем на 30 дней лишает статуса UKR.",
      "Wyjazd z Polski na dłużej niż 30 dni odbiera status UKR.",
      "Leaving Poland for more than 30 days ends the UKR status."),
      q(mswia, "opuściły terytorium Polski na okres powyżej 30 dni,")),
    "lost_other_permit" -> Reason(Words(
      "Інший дозвіл на перебування або захист у Польщі замінює статус UKR — його втрачено.",
      "Другое разрешение на пребывание или защита в Польше заменяет статус UKR — он утрачен.",
      "Inne zezwolenie na pobyt lub ochrona w Polsce zastępuje status UKR — został utracony.",
      "Another residence permit or protection in Poland replaces the UKR status — it is lost."),
      q(mswia, "Status UKR stracą również osoby, które:")),
    "lost_protection_application" -> Reason(Words(
      "Заява на міжнародний захист позбавляє статусу UKR.",
      "Заявление о международной защите лишает статуса UKR.", "Wniosek o ochronę międzynarodową odbiera status UKR.",
      "An application for international protection ends the UKR status."),
      q(mswia, "Status UKR stracą również osoby, które:")),
    "lost_other_eu_state" -> Reason(Words(
      "Тимчасовий захист в іншій державі ЄС позбавляє статусу UKR у Польщі.",
      "Временная защита в другой стране ЕС лишает статуса UKR в Польше.",
      "Ochrona czasowa w innym państwie UE odbiera status UKR w Polsce.",
      "Temporary protection in another EU state ends the UKR status in Poland."),
      q(mswia, "Status UKR stracą również osoby, które:")),
    "lost_citizenship" -> Reason(Words(
      "Громадянство Польщі або іншої держави ЄС замінює статус UKR.",
      "Гражданство Польши или другой страны ЕС заменяет статус UKR.",
      "Obywatelstwo polskie lub innego państwa UE zastępuje status UKR.",
      "Polish or another EU citizenship replaces the UKR status."),
      q(mswia, "Status UKR stracą również osoby, które:")),
    "lost_no_passport" -> Reason(Words(
      "Хто був зареєстрований без паспорта і не показав його в гміні до 31 серпня 2026 р., втратив PESEL UKR.",
      "Кто был зарегистрирован без паспорта и не показал его в гмине до 31 августа 2026 г., утратил PESEL UKR.",
      "Kto był zarejestrowany bez paszportu i nie okazał go w gminie do 31 sierpnia 2026 r., utracił PESEL UKR.",
      "One registered without a passport who did not show it at a municipal office by 31 August 2026 lost PESEL UKR."),
      q(mswia, "dokument podróży (paszport) w dowolnym urzędzie gminy do 31 sierpnia 2026 r.")),
    "pesel_by_registration" -> Reason(Words(
      "Зареєструйте місце проживання (замельдування) — PESEL буде присвоєно автоматично.",
      "Зарегистрируйте место жительства (замельдование) — PESEL будет присвоен автоматически.",
      "Zamelduj się — numer PESEL zostanie nadany automatycznie.",
      "Register your residence (zameldowanie) — a PESEL is given automatically."),
      q(peselPage,
        ("Jeśli jesteś cudzoziemcem i mieszkasz w Polsce, możesz się zameldować. Wtedy automatycznie otrzymasz numer " +
        "PESEL.")),
      good = true),
    "pesel_by_application" -> Reason(Words(
      ("Подайте заяву про присвоєння PESEL особисто (з 2026 р. — лише особисто), безкоштовно, вказавши правову " +
        "підставу, з якої випливає обов'язок мати PESEL."),
      ("Подайте заявление о присвоении PESEL лично (с 2026 г. — только лично), бесплатно, указав правовое " +
        "основание, из которого следует обязанность иметь PESEL."),
      ("Złóż wniosek o nadanie PESEL osobiście (od 2026 r. tylko osobiście), bezpłatnie, wpisując podstawę prawną, " +
        "z której wynika obowiązek posiadania numeru."),
      "Apply for a PESEL in person (from 2026 in person only), free, naming the law that requires you to have it."),
      q(peselPage, "We wniosku wpisz faktyczną podstawę prawną, z której wynika obowiązek posiadania numeru PESEL."),
      Vector(q(peselPage, "Nie musisz płacić za złożenie tego wniosku.")), good = true),
    "no_basis_for_pesel_yet" -> Reason(Words(
      ("Без замельдування PESEL дають за заявою лише тоді, коли закон вимагає мати номер (напр., ZUS чи податкова " +
        "вкажуть підставу)."),
      ("Без замельдования PESEL дают по заявлению только тогда, когда закон требует иметь номер (напр., ZUS или " +
        "налоговая укажут основание)."),
      ("Bez zameldowania PESEL nadaje się na wniosek tylko wtedy, gdy przepis wymaga posiadania numeru (np. ZUS " +
        "czy urząd skarbowy wskażą podstawę)."),
      ("Without registering, a PESEL is given on application only when a law requires the number (ZUS or the tax " +
        "office will name it).")),
      q(peselPage,
        ("Jeśli jakiś urząd (np. Zakład Ubezpieczeń Społecznych czy urząd skarbowy) wymaga podania numeru PESEL – " +
        "powinien wskazać Ci również podstawę prawną."))))

  private def topic(model: Model, name: Words, yesTitle: Words, noTitle: Words, questions: Question*): Topic =
    Topic(name, model, questions.toVector, reasons, Vector(difference), book, fetched, Some(yesTitle), Some(noTitle))

  def topics(model: Model): Vector[Topic] = Vector(
    topic(model, Words("Як отримати PESEL UKR?", "Как получить PESEL UKR?", "Jak uzyskać PESEL UKR?",
      "How to get PESEL UKR?"),
      Words("PESEL UKR можна отримати", "PESEL UKR можно получить", "PESEL UKR można uzyskać", "PESEL UKR can be got"),
      Words("PESEL UKR — ні", "PESEL UKR — нет", "PESEL UKR — nie", "No PESEL UKR"),
      Question("who", Words("Хто ви?", "Кто вы?", "Kim jesteś?", "Who are you?"), Vector(
        Choice("ua", Words("Громадянин України, жив(ла) в Україні до 24.02.2022, або член сім'ї",
          "Гражданин Украины, жил(а) в Украине до 24.02.2022, или член семьи",
          "Obywatel Ukrainy mieszkający w Ukrainie przed 24.02.2022 lub członek rodziny",
          "A Ukrainian citizen who lived in Ukraine before 24 Feb 2022, or a family member"), Pesel.ukrainian),
        Choice("protected", Words("Іноземець, який мав захист в Україні до 24.02.2022, або член сім'ї",
          "Иностранец с защитой в Украине до 24.02.2022 или член семьи",
          "Cudzoziemiec z ochroną w Ukrainie przed 24.02.2022 lub członek rodziny",
          "A foreigner with protection in Ukraine before 24 Feb 2022, or a family member"), Pesel.protectedThere),
        Choice("resident",
          Words("Іноземець, який законно жив в Україні до 24.02.2022 і не може безпечно повернутися додому",
          "Иностранец, законно живший в Украине до 24.02.2022, который не может безопасно вернуться домой",
          "Cudzoziemiec legalnie przebywający w Ukrainie przed 24.02.2022, który nie może bezpiecznie wrócić do kraju",
          "A foreigner who stayed legally in Ukraine before 24 Feb 2022 and cannot return home safely"),
            Pesel.residentThere),
        Choice("none", Words("Ніхто з цих", "Никто из этих", "Nikt z nich", "None of these"), Pesel.nobody))),
      Question("crossed", Words("Коли ви перетнули кордон Польщі?", "Когда вы пересекли границу Польши?",
        "Kiedy przekroczyłeś(-aś) granicę Polski?", "When did you cross the Polish border?"), Vector(
        Choice("in", Words("30 днів тому або менше", "30 дней назад или меньше", "30 dni temu lub mniej",
          "30 days ago or less"),
          Pesel.crossed(30)),
        Choice("late", Words("Більше 30 днів тому", "Больше 30 дней назад", "Ponad 30 dni temu",
          "More than 30 days ago"),
          Pesel.crossed(31)))),
      Question("passport", Words("Чи є у вас чинний паспорт?", "Есть ли у вас действующий паспорт?",
        "Czy masz ważny paszport?", "Do you have a valid passport?"),
        Vector(Choice("yes", yes, Pesel.passport(true)), Choice("no", no, Pesel.passport(false)))),
      Question("excluded", Words("Чи щось із цього стосується вас?", "Относится ли к вам что-то из этого?",
        "Czy coś z tego dotyczy Ciebie?", "Does any of these apply to you?"), Vector(
        Choice("none", Words("Нічого з цього", "Ничего из этого", "Nic z tego", "None of these"), Pesel.noExclusion),
        Choice("permit", Words("Маю в Польщі дозвіл на перебування або захист",
          "Есть в Польше разрешение на пребывание или защита",
          "Mam w Polsce zezwolenie na pobyt lub ochronę", "I hold a residence permit or protection in Poland"),
            Pesel.holdsPermit),
        Choice("application", Words("Подав(ла) заяву на міжнародний захист у Польщі",
          "Подал(а) заявление о международной защите в Польше",
          "Złożyłem(-am) wniosek o ochronę międzynarodową w Polsce",
            "I applied for international protection in Poland"),
          Pesel.appliedProtection),
        Choice("other", Words("Маю тимчасовий захист в іншій державі ЄС", "Есть временная защита в другой стране ЕС",
          "Mam ochronę czasową w innym państwie UE", "I have temporary protection in another EU state"),
            Pesel.otherState),
        Choice("border", Words("В'їхав(ла) за дозволом на місцевий прикордонний рух",
          "Въехал(а) по разрешению на местное приграничное движение",
          "Wjechałem(-am) na podstawie zezwolenia na mały ruch graniczny",
            "I entered on a local border traffic permit"),
          Pesel.borderTraffic)))),
    topic(model, Words("Чи збережеться мій статус UKR?", "Сохранится ли мой статус UKR?", "Czy zachowam status UKR?",
      "Will I keep the UKR status?"),
      Words("Статус UKR зберігається", "Статус UKR сохраняется", "Status UKR zostaje", "The UKR status is kept"),
      Words("Статус UKR втрачено", "Статус UKR утрачен", "Status UKR utracony", "The UKR status is lost"),
      Question("away", Words("Найдовша відсутність у Польщі за раз?", "Самое долгое отсутствие в Польше за раз?",
        "Najdłuższa jednorazowa nieobecność w Polsce?", "The longest absence from Poland at a stretch?"), Vector(
        Choice("ok", Words("30 днів або менше", "30 дней или меньше", "30 dni lub mniej", "30 days or less"),
          Pesel.away(30)),
        Choice("long", Words("Більше 30 днів", "Больше 30 дней", "Ponad 30 dni", "More than 30 days"),
          Pesel.away(31)))),
      Question("else", Words("Чи сталося щось із цього?", "Случилось ли что-то из этого?",
        "Czy zdarzyło się coś z tego?",
        "Has any of these happened?"), Vector(
        Choice("nothing", Words("Нічого з цього", "Ничего из этого", "Nic z tego", "None of these"), Pesel.nothingElse),
        Choice("permit", Words("Отримав(ла) дозвіл на перебування або захист у Польщі",
          "Получил(а) разрешение на пребывание или защиту в Польше",
          "Dostałem(-am) zezwolenie na pobyt lub ochronę w Polsce",
            "I got a residence permit or protection in Poland"), Pesel.gotPermit),
        Choice("application", Words("Подав(ла) заяву на міжнародний захист",
          "Подал(а) заявление о международной защите",
          "Złożyłem(-am) wniosek o ochronę międzynarodową", "I applied for international protection"),
            Pesel.appliedLater),
        Choice("other", Words("Отримав(ла) тимчасовий захист в іншій державі ЄС",
          "Получил(а) временную защиту в другой стране ЕС",
          "Dostałem(-am) ochronę czasową w innym państwie UE", "I got temporary protection in another EU state"),
            Pesel.gotOtherState),
        Choice("citizen", Words("Став(ла) громадянином Польщі чи іншої держави ЄС",
          "Стал(а) гражданином Польши или другой страны ЕС",
          "Zostałem(-am) obywatelem Polski lub innego państwa UE", "I became a Polish or another EU citizen"),
            Pesel.citizen),
        Choice("passport", Words("Мене зареєстрували без паспорта, і я не показав(ла) його в гміні до 31.08.2026",
          "Меня зарегистрировали без паспорта, и я не показал(а) его в гмине до 31.08.2026",
          "Zarejestrowano mnie bez paszportu i nie okazałem(-am) go w gminie do 31.08.2026",
          "I was registered without a passport and did not show one by 31 Aug 2026"), Pesel.noPassport)))),
    topic(model, Words("Звичайний PESEL: як отримати?", "Обычный PESEL: как получить?", "Zwykły PESEL: jak uzyskać?",
      "An ordinary PESEL: how to get one?"),
      Words("Як отримати PESEL", "Как получить PESEL", "Jak uzyskać PESEL", "How to get a PESEL"),
      Words("Поки що підстави немає", "Пока основания нет", "Na razie brak podstawy", "No basis yet"),
      Question("registers", Words("Чи реєструєте ви місце проживання в Польщі (замельдування)?",
        "Регистрируете ли вы место жительства в Польше (замельдование)?", "Czy meldujesz się w Polsce?",
        "Do you register your residence in Poland (zameldowanie)?"),
        Vector(Choice("yes", yes, Pesel.registers(true)), Choice("no", no, Pesel.registers(false)))),
      Question("required", Words("Чи вимагає від вас установа (ZUS, податкова тощо) PESEL на підставі закону?",
        "Требует ли от вас учреждение (ZUS, налоговая и т. п.) PESEL на основании закона?",
        "Czy urząd (ZUS, urząd skarbowy itd.) wymaga od Ciebie numeru PESEL na podstawie przepisu?",
        "Does an office (ZUS, the tax office…) require a PESEL of you under a law?"),
        Vector(Choice("yes", yes, Pesel.required(true)), Choice("no", no, Pesel.required(false))))))
