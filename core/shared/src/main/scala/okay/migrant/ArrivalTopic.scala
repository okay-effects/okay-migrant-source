package okay.migrant

import okay.study.Model

/** I HAVE COME TO POLAND: WHAT DO I DO? — the guide as a topic: who the person is, how long they stay, what for; the
 * answer is the steps in order, each with the office's sentence it rests on and, where the assistant says more of
 * it, the topic to open (`Reason.see`). The quotes are the offices' (`books/pl/arrival/` and the books it leans on),
 * the plain wordings ours and unofficial */
object ArrivalTopic:
  private val fetched = "2026-11-20"
  private val book = Words("Джерело: портал MOS Управління у справах іноземців (UdSC), MSWiA, Управа міста Варшави; отримано",
    "Источник: портал MOS Управления по делам иностранцев (UdSC), MSWiA, Управа города Варшавы; получено",
    "Źródło: portal MOS Urzędu do Spraw Cudzoziemców (UdSC), MSWiA, Urząd m.st. Warszawy; pobrane",
    "Source: the Office for Foreigners' MOS portal (UdSC), MSWiA, the City of Warsaw; fetched")

  private val cukrSheet = "UdSC, «Informacja dotycząca procedury wydania karty pobytu CUKR»"
  private val cukrFaq = "UdSC, «Karta pobytu CUKR – pytania i odpowiedzi»"
  private val mswia = "MSWiA: «Najczęściej zadawane pytania i odpowiedzi» (status UKR)"
  private val peselPage = "Urząd m.st. Warszawy, 19115: «Nadanie numeru PESEL na wniosek cudzoziemca…»"
  private val workPage = "UdSC, MOS: «Zezwolenie na pobyt czasowy i pracę (zezwolenie jednolite)»"
  private val stalyPage = "UdSC, MOS: «Zezwolenie na pobyt stały»"
  private val euPage = "UdSC, MOS: «Rejestracja pobytu obywatela UE»"
  private val rulesPage = "UdSC, MOS: «Ogólne zasady wykonywania przez cudzoziemców pracy»"
  private val visaFreePage = "UdSC, MOS: «Ruch bezwizowy»"
  private val visasPage = "UdSC, MOS: «Wizy»"
  private val studiesPage = "UdSC, MOS: «Zezwolenie na pobyt czasowy w celu kształcenia się na studiach»"
  private val purposesPage = "UdSC, MOS: «Zezwolenie na pobyt czasowy — cel pobytu»"

  private def pl(page: String, quote: String) = Source(page, Map("pl" -> quote))
  private def step(text: Words, source: Source, also: Vector[Source] = Vector.empty, see: String = ""): Reason =
    Reason(text, source, also, good = true, see = see.split('/').toVector.filter(_.nonEmpty))

  val reasons: Map[String, Reason] = Map(
    "s1_pesel_ukr_in_30_days" -> step(Words(
      "1. Протягом 30 днів після перетину кордону зверніться до будь-якого уряду гміни по PESEL зі статусом UKR — з паспортом. Це безкоштовно.",
      "1. В течение 30 дней после пересечения границы обратитесь в любой уряд гмины за PESEL со статусом UKR — с паспортом. Это бесплатно.",
      "1. W ciągu 30 dni od przekroczenia granicy zgłoś się do dowolnego urzędu gminy po PESEL ze statusem UKR — z paszportem. To bezpłatne.",
      "1. Within 30 days of crossing the border, go to any municipal office for a PESEL with the UKR status — with your passport. It is free."),
      pl(mswia, "Zgłosi się w dowolnym urzędzie gminy w Polsce najpóźniej w ciągu 30 dni od przekroczenia granicy Polski. Przekroczenie tego terminu uniemożliwia otrzymanie statusu UKR."),
      see = "pesel/how-to-get-pesel-ukr"),
    "s2_ukr_work_without_permit" -> step(Words(
      "2. Зі статусом UKR ви перебуваєте легально (до 4 березня 2027 р.) і можете працювати без дозволу на роботу.",
      "2. Со статусом UKR вы находитесь легально (до 4 марта 2027 г.) и можете работать без разрешения на работу.",
      "2. Ze statusem UKR przebywasz legalnie (do 4 marca 2027 r.) i możesz pracować bez zezwolenia na pracę.",
      "2. With the UKR status your stay is legal (until 4 March 2027) and you may work with no work permit."),
      Source(cukrFaq, Map("pl" -> "Status ten umożliwia legalny pobyt w Polsce do 4 marca 2027 r. Daje uprawnienia do pracy bez dodatkowych zezwoleń",
        "uk" -> "дозволяє легально перебувати в Польщі до 4 березня 2027 р."))),
    "s3_ukr_away_at_most_30_days" -> step(Words(
      "3. Не виїжджайте з Польщі більш ніж на 30 днів поспіль: статус UKR буде втрачено.",
      "3. Не уезжайте из Польши более чем на 30 дней подряд: статус UKR будет утрачен.",
      "3. Nie wyjeżdżaj z Polski na dłużej niż 30 dni: status UKR zostanie utracony.",
      "3. Do not leave Poland for more than 30 days at a stretch: the UKR status would be lost."),
      Source(cukrFaq, Map("pl" -> "Status UKR jest anulowany w przypadku wyjazdu z Polski na okres powyżej 30 dni.",
        "uk" -> "Статус UKR анулюється у разі виїзду з Польщі на строк понад 30 днів.")),
      see = "pesel/will-i-keep-the-ukr-status"),
    "s4_ukr_cukr_after_365_days" -> step(Words(
      "4. Коли статус UKR триватиме безперервно щонайменше 365 днів, можна подати заяву на посвідку CUKR (до 4 березня 2027 р.).",
      "4. Когда статус UKR продлится непрерывно не менее 365 дней, можно подать заявление на карту CUKR (до 4 марта 2027 г.).",
      "4. Gdy status UKR potrwa nieprzerwanie co najmniej 365 dni, możesz złożyć wniosek o kartę pobytu CUKR (do 4 marca 2027 r.).",
      "4. Once the UKR status has lasted at least 365 days without a break, you may apply for the CUKR card (until 4 March 2027)."),
      Source(cukrSheet, Map("pl" -> "posiadałeś status UKR nieprzerwanie przez co najmniej 365 dni",
        "uk" -> "ви мали статус UKR безперервно щонайменше протягом 365 днів")),
      see = "cukr/can-i-apply-for-the-cukr-card"),
    "s1_eu_three_months_free" -> step(Words(
      "1. Як громадянин ЄС ви можете перебувати в Польщі до 3 місяців (до 6, якщо активно шукаєте роботу) без формальностей, крім реєстрації адреси (мельдунку).",
      "1. Как гражданин ЕС вы можете находиться в Польше до 3 месяцев (до 6, если активно ищете работу) без формальностей, кроме регистрации адреса (мельдунка).",
      "1. Jako obywatel UE możesz przebywać w Polsce do 3 miesięcy (do 6, jeśli aktywnie szukasz pracy) bez formalności poza meldunkowymi.",
      "1. As an EU citizen you may stay in Poland up to 3 months (up to 6 if actively seeking work) with no formalities but registering an address."),
      pl(euPage, "Posiadając obywatelstwo UE możesz przebywać w Polsce bez konieczności dopełniania formalności, innych niż meldunkowe, przez okres do 3 miesięcy lub do 6 miesięcy, jeśli aktywnie poszukujesz pracy i masz rzeczywiste szanse na jej znalezienie.")),
    "s2_eu_register_stay" -> step(Words(
      "2. Щоб залишитися довше, зареєструйте перебування у воєводи за місцем проживання — особисто (через MOS поки не можна), безкоштовно; посвідчення дійсне 10 років.",
      "2. Чтобы остаться дольше, зарегистрируйте пребывание у воеводы по месту жительства — лично (через MOS пока нельзя), бесплатно; свидетельство действует 10 лет.",
      "2. Aby zostać dłużej, zarejestruj pobyt u wojewody właściwego dla miejsca pobytu — osobiście (przez MOS na razie nie), bezpłatnie; zaświadczenie ważne 10 lat.",
      "2. To stay longer, register your stay with the voivode where you live — in person (not through MOS yet), free; the certificate is valid 10 years."),
      pl(euPage, "Jeżeli chcesz przebywać dłużej, musisz spełniać warunki pobytu określone w przepisach prawa oraz powinieneś zarejestrować swój pobyt w Polsce."),
      Vector(pl(euPage, "na wniosek złożony przez obywatela UE osobiście. Złożenie wniosku poprzez MOS nie jest na ten moment możliwe."))),
    "s3_eu_work_free" -> step(Words(
      "3. Працювати можна без дозволу на роботу: громадяни ЄС, ЄЕЗ і Швейцарії мають вільний доступ до ринку праці.",
      "3. Работать можно без разрешения на работу: граждане ЕС, ЕЭЗ и Швейцарии имеют свободный доступ к рынку труда.",
      "3. Możesz pracować bez zezwolenia na pracę: obywatele UE, EOG i Szwajcarii mają swobodny dostęp do rynku pracy.",
      "3. You may work with no work permit: citizens of the EU, the EEA and Switzerland have free access to the labour market."),
      pl(rulesPage, "Z takiej swobody korzystają przede wszystkim obywatele UE, EOG (NO, IS, LI), Szwajcarii oraz UK - beneficjenci Brexitu, a także określeni członkowie ich rodzin.")),
    "s1_count_90_in_180_days" -> step(Words(
      "1. Без візи або за шенгенською візою можна бути не більше 90 днів у кожні 180 — рахуються дні в усіх країнах Шенгену разом.",
      "1. Без визы или по шенгенской визе можно находиться не более 90 дней в каждые 180 — считаются дни во всех странах Шенгена вместе.",
      "1. Bez wizy lub na wizie Schengen możesz przebywać najwyżej 90 dni w każdych 180 — liczą się dni we wszystkich państwach Schengen razem.",
      "1. Without a visa or on a Schengen visa you may stay at most 90 days in any 180 — days in all Schengen states count together."),
      pl(visaFreePage, "Dopuszczalny okres pobytu na tej podstawie w strefie Schengen, w tym w Polsce, wynosi 90 dni w okresie 180 - dniowym."),
      Vector(pl(visasPage, "Pobyt krótkoterminowy to taki, który trwa nie dłużej niż 90 dni w każdym okresie 180 dni."))),
    "s2_work_needs_a_permit" -> step(Words(
      "2. Щоб працювати, потрібна підстава: дозвіл на роботу або заява про доручення роботи (їх отримує роботодавець), або дозвіл на проживання і роботу — чи ситуація, що звільняє від них.",
      "2. Чтобы работать, нужно основание: разрешение на работу или заявление о поручении работы (их получает работодатель), либо разрешение на пребывание и работу — или ситуация, освобождающая от них.",
      "2. Aby pracować, potrzebujesz podstawy: zezwolenia na pracę lub oświadczenia o powierzeniu pracy (uzyskuje je pracodawca), zezwolenia na pobyt i pracę — albo sytuacji, która z nich zwalnia.",
      "2. To work you need a basis: a work permit or a declaration of entrusting work (the employer gets these), a residence and work permit — or a situation that frees you from them."),
      pl(rulesPage, "muszą być uprawnieni do wykonywania w Polsce pracy - uprawnienie to może wynikać bądź z dokumentu pozwalającego im na pracę w Polsce, który uzyskał dla nich pracodawca (np. zezwolenie na pracę) lub, który sami uzyskali (np. zezwolenie na pobyt czasowy i pracę), bądź z określonej sytuacji, która sprawia, iż mogą wykonywać w Polsce pracę bez tego dokumentu.")),
    "s3_apply_while_stay_is_legal" -> step(Words(
      "3. Подайте заяву на дозвіл на тимчасове проживання, поки ваше перебування легальне (до кінця візи чи 90 днів). Якщо заяву подано правильно і вчасно, ви перебуваєте легально до рішення.",
      "3. Подайте заявление на разрешение на временное пребывание, пока ваше пребывание легально (до конца визы или 90 дней). Если заявление подано правильно и вовремя, вы находитесь легально до решения.",
      "3. Złóż wniosek o zezwolenie na pobyt czasowy, póki twój pobyt jest legalny (do końca wizy lub 90 dni). Wniosek złożony prawidłowo i w porę czyni pobyt legalnym do decyzji.",
      "3. Apply for a temporary residence permit while your stay is legal (before your visa or 90 days end). Filed correctly and in time, the application keeps your stay legal until the decision."),
      pl(workPage, "Jeżeli wniosek został złożony prawidłowo i w trakcie legalnego pobytu, dalszy pobyt cudzoziemca uważa się za legalny od dnia złożenia wniosku do dnia, w którym decyzja w sprawie zezwolenia stanie się ostateczna.")),
    "s4_single_permit" -> step(Words(
      "4. Для роботи довше 3 місяців — дозвіл на тимчасове проживання і роботу (єдиний дозвіл), через портал MOS; роботодавець заповнює додаток № 1.",
      "4. Для работы дольше 3 месяцев — разрешение на временное пребывание и работу (единое разрешение), через портал MOS; работодатель заполняет приложение № 1.",
      "4. Do pracy dłuższej niż 3 miesiące — zezwolenie na pobyt czasowy i pracę (zezwolenie jednolite), przez MOS; pracodawca wypełnia załącznik nr 1.",
      "4. For work longer than 3 months — the temporary residence and work permit (the single permit), through MOS; the employer fills in annex no. 1."),
      pl(workPage, "Wykonywanie pracy musi stanowić podstawę ubiegania się o zezwolenie na pobyt czasowy i pracę, a jednocześnie musi uzasadniać pobyt w Polsce powyżej 3 miesięcy."),
      see = "work"),
    "s5_visa_purpose_may_exclude" -> step(Words(
      "5. Перевірте мету вашої візи (шенгенської чи національної): з візою для туризму чи відвідин рідних єдиний дозвіл отримати не можна.",
      "5. Проверьте цель вашей визы (шенгенской или национальной): с визой для туризма или посещения родных единое разрешение получить нельзя.",
      "5. Sprawdź cel swojej wizy (Schengen lub krajowej): na wizie turystycznej lub w celu odwiedzin rodziny zezwolenia jednolitego uzyskać nie można.",
      "5. Check your visa's purpose (Schengen or national): on a visa for tourism or to visit family the single permit cannot be got."),
      pl(workPage, "przebywający w Polsce na podstawie wizy (typu D lub Schengen) wydanej przez konsula RP w celu:"),
      Vector(pl(workPage, "przebywający w Polsce na podstawie wizy w celu turystycznym lub w celu odwiedzin u rodziny lub przyjaciół, wydanej przez inne państwo obszaru Schengen;")),
      see = "work"),
    "s4_study_permit" -> step(Words(
      "4. Для денних студій у затвердженому виші — дозвіл на тимчасове проживання з метою навчання на студіях, через портал MOS.",
      "4. Для очной учёбы в утверждённом вузе — разрешение на временное пребывание с целью обучения, через портал MOS.",
      "4. Na studia stacjonarne w zatwierdzonej uczelni — zezwolenie na pobyt czasowy w celu kształcenia się na studiach, przez MOS.",
      "4. For full-time studies at an approved university — the temporary residence permit for studies, through MOS."),
      pl(studiesPage, "Zezwolenie na pobyt czasowy w celu kształcenia się na studiach uprawnia do tymczasowego przebywania w Polsce w celu rozpoczęcia lub kontynuowania studiów.")),
    "s5_studies_work_without_permit" -> step(Words(
      "5. З цим дозволом можна працювати без окремого дозволу на роботу.",
      "5. С этим разрешением можно работать без отдельного разрешения на работу.",
      "5. Z tym zezwoleniem możesz pracować bez osobnego zezwolenia na pracę.",
      "5. With this permit you may work with no separate work permit."),
      pl(rulesPage, "Swobodny dostęp do polskiego rynku pracy dotyczy sytuacji, kiedy możesz wykonywać pracę bez konieczności dopełniania dodatkowych formalności administracyjnych."),
      Vector(pl(rulesPage, "w celu kształcenia się na studiach (art. 144**);"))),
    "s4_spouse_permit" -> step(Words(
      "4. Подружжю громадянина Польщі — дозвіл на тимчасове проживання у зв'язку зі шлюбом; з ним можна працювати без дозволу на роботу.",
      "4. Супругу гражданина Польши — разрешение на временное пребывание в связи с браком; с ним можно работать без разрешения на работу.",
      "4. Małżonkowi obywatela polskiego — zezwolenie na pobyt czasowy z uwagi na małżeństwo; z nim możesz pracować bez zezwolenia na pracę.",
      "4. For the spouse of a Polish citizen — a temporary residence permit for the marriage; with it you may work with no work permit."),
      pl(rulesPage, "Pełen dostęp do rynku pracy mają także ci obywatele państw trzecich i bezpaństwowcy, którzy posiadają zezwolenie na pobyt udzielane bezterminowo (m.in. na pobyt stały, status uchodźcy) lub których szczególna sytuacja rodzinna lub prawna pozwala na pobyt w Polsce i wykonywanie pracy bez zezwolenia (np. małżonkowie obywateli polskich, którym udzielono zezwolenia na pobyt czasowy z uwagi na małżeństwo).")),
    "s5_permanent_later" -> step(Words(
      "5. Пізніше — дозвіл на постійне проживання: шлюб щонайменше 3 роки і 2 роки безперервного перебування на дозволі у зв'язку з ним.",
      "5. Позже — разрешение на постоянное проживание: брак не менее 3 лет и 2 года непрерывного пребывания на разрешении в связи с ним.",
      "5. Później — zezwolenie na pobyt stały: małżeństwo co najmniej 3 lata i 2 lata nieprzerwanego pobytu na zezwoleniu z jego tytułu.",
      "5. Later — the permanent residence permit: a marriage of at least 3 years and 2 years without a break on the permit for it."),
      Source(stalyPage, Map(
        "pl" -> "pozostawał w tym związku małżeńskim przez co najmniej 3 lata przed dniem złożenia wniosku o udzielenie mu zezwolenia na pobyt stały",
        "uk" -> "перебуває у шлюбі щонайменше 3 роки до дати подання заяви на отримання посвідки на постійне проживання",
        "ru" -> "состоял в браке не менее 3 лет до даты подачи заявления на получение постоянного вида на жительство")),
      see = "permanent"),
    "s4_other_purposes" -> step(Words(
      "4. Дозвіл на тимчасове проживання видають за основною метою: робота, бізнес, наукові дослідження, сім'я, студії, навчання, стажування, волонтерство, інші обставини — оберіть свою на порталі MOS. Цей помічник поки описує роботу, студії та шлюб.",
      "4. Разрешение на временное пребывание выдают по основной цели: работа, бизнес, научные исследования, семья, учёба в вузе, обучение, стажировка, волонтёрство, другие обстоятельства — выберите свою на портале MOS. Этот помощник пока описывает работу, учёбу и брак.",
      "4. Zezwolenie na pobyt czasowy udziela się ze względu na główny cel: praca, działalność gospodarcza, badania naukowe, rodzina, studia, nauka, staż, wolontariat, inne okoliczności — wybierz swój w MOS. Ten asystent opisuje na razie pracę, studia i małżeństwo.",
      "4. A temporary residence permit is given for a main purpose: work, business, research, family, studies, schooling, an internship, volunteering, other circumstances — choose yours in MOS. This assistant so far covers work, studies and marriage."),
      pl(purposesPage, "Wybierz główny cel pobytu")),
    "s9_register_residence_pesel" -> step(Words(
      "Наостанок: зареєструйте місце проживання (замельдування) — тоді PESEL присвоять автоматично.",
      "Напоследок: зарегистрируйте место жительства (замельдование) — тогда PESEL присвоят автоматически.",
      "Na koniec: zamelduj się — wtedy numer PESEL zostanie nadany automatycznie.",
      "Finally: register your residence (zameldowanie) — then a PESEL is given automatically."),
      pl(peselPage, "Jeśli jesteś cudzoziemcem i mieszkasz w Polsce, możesz się zameldować. Wtedy automatycznie otrzymasz numer PESEL."),
      see = "pesel/an-ordinary-pesel-how-to-get-one"))

  def topic(model: Model): Topic = Topic(
    Words("Я приїхав(ла) до Польщі: що робити?", "Я приехал(а) в Польшу: что делать?",
      "Przyjechałem(-am) do Polski: co robić?", "I have come to Poland: what do I do?"),
    model,
    Vector(
      Question("who", Words("Хто ви і як ви приїхали?", "Кто вы и как вы приехали?", "Kim jesteś i jak przyjechałeś(-aś)?",
        "Who are you and how did you come?"), Vector(
        Choice("ukraine", Words("Приїхав(ла) з України через війну (з 24.02.2022), або член сім'ї",
          "Приехал(а) из Украины из-за войны (с 24.02.2022), или член семьи",
          "Przyjechałem(-am) z Ukrainy z powodu wojny (od 24.02.2022) lub członek rodziny",
          "I came from Ukraine because of the war (since 24 Feb 2022), or a family member"), Arrival.ukraine),
        Choice("eu", Words("Громадянин ЄС, Норвегії, Ісландії, Ліхтенштейну чи Швейцарії",
          "Гражданин ЕС, Норвегии, Исландии, Лихтенштейна или Швейцарии",
          "Obywatel UE, Norwegii, Islandii, Liechtensteinu lub Szwajcarii",
          "A citizen of the EU, Norway, Iceland, Liechtenstein or Switzerland"), Arrival.eu),
        Choice("visa_free", Words("З іншої країни, без візи (безвізовий в'їзд)", "Из другой страны, без визы (безвизовый въезд)",
          "Z innego kraju, bez wizy (ruch bezwizowy)", "From another country, without a visa"), Arrival.visaFree),
        Choice("schengen", Words("З іншої країни, за шенгенською візою (тип C)", "Из другой страны, по шенгенской визе (тип C)",
          "Z innego kraju, na wizie Schengen (typ C)", "From another country, on a Schengen visa (type C)"), Arrival.schengen),
        Choice("national", Words("З іншої країни, за національною візою Польщі (тип D)",
          "Из другой страны, по национальной визе Польши (тип D)", "Z innego kraju, na wizie krajowej (typ D)",
          "From another country, on a Polish national visa (type D)"), Arrival.national))),
      Question("long", Words("Скільки ви плануєте пробути в Польщі?", "Сколько вы планируете пробыть в Польше?",
        "Jak długo planujesz zostać w Polsce?", "How long do you plan to stay in Poland?"), Vector(
        Choice("short", Words("До 90 днів", "До 90 дней", "Do 90 dni", "Up to 90 days"), Arrival.long(false)),
        Choice("long", Words("Довше 90 днів", "Дольше 90 дней", "Dłużej niż 90 dni", "Longer than 90 days"),
          Arrival.long(true)))),
      Question("purpose", Words("Яка головна мета?", "Какая главная цель?", "Jaki jest główny cel?",
        "What is the main purpose?"), Vector(
        Choice("work", Words("Робота", "Работа", "Praca", "Work"), Arrival.work),
        Choice("studies", Words("Денні студії у виші", "Очная учёба в вузе", "Studia stacjonarne",
          "Full-time university studies"), Arrival.studies),
        Choice("spouse", Words("Життя з чоловіком / дружиною — громадянином Польщі",
          "Жизнь с мужем / женой — гражданином Польши", "Życie z małżonkiem — obywatelem Polski",
          "Life with a spouse who is a Polish citizen"), Arrival.spouse),
        Choice("other", Words("Інше (туризм, бізнес, сім'я…)", "Другое (туризм, бизнес, семья…)",
          "Inne (turystyka, działalność, rodzina…)", "Other (tourism, business, family…)"), Arrival.other)))),
    reasons, Vector.empty, book, fetched,
    Some(Words("Ваші кроки", "Ваши шаги", "Twoje kroki", "Your steps")), None)
