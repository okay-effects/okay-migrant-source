package okay.migrant

import okay.study.Model

/** WHAT FOLLOWS THE CUKR CARD'S APPLICATION, as topics of the assistant — a situation a topic, one model for all
 * (`models/pl-cukr-after.json`): leaving after the application, collecting the card, being away, moving, a
 * refusal, a lost card, work. A number is answered by its side of the rule's threshold. The quotes are the
 * office's, from `books/pl/cukr/` (Polish and Ukrainian); the plain wordings are ours and unofficial */
object CukrAfterTopic:
  private val sheet = "UdSC, «Informacja dotycząca procedury wydania karty pobytu CUKR»"
  private val faq = "UdSC, «Karta pobytu CUKR – pytania i odpowiedzi»"
  private val book = Words("Джерело: матеріали Управління у справах іноземців (UdSC), отримані",
    "Источник: материалы Управления по делам иностранцев (UdSC), получены",
    "Źródło: materiały Urzędu do Spraw Cudzoziemców (UdSC), pobrane",
    "Source: the Office for Foreigners' (UdSC) materials, fetched")
  private val fetched = "2026-11-19"
  private def source(pl: String, uk: String, place: String = faq) = Source(place, Map("pl" -> pl, "uk" -> uk))

  private val thirty = source(
    "Samo złożenie wniosku o wydanie karty pobytu CUKR nie pozbawia Cię statusu UKR, zatem możesz wyjechać z Polski, jednak nie dłużej niż na 30 dni.",
    "Саме подання заяви про видачу посвідки на проживання CUKR не позбавляє вас статусу UKR, тому ви можете виїхати з Польщі, але не більш, ніж на 30 днів.")
  private val sixty = source(
    "Twoja karta zostanie unieważniona i nie będziesz mógł jej odebrać. Nie będziesz mógł się ubiegać o zwrot dokonanych opłat.",
    "Ваша посвідка буде анульована, і ви не зможете її отримати. Ви не зможете звернутися за поверненням сплачених платежів.")
  private val stretch = source(
    "Aby nie utracić karty pobytu CUKR nie możesz wyjechać z Polski jednorazowo na okres 6 miesięcy.",
    "Щоб не втратити посвідку на проживання CUKR, ви не можете виїхати з Польщі одноразово на строк 6 місяців.")
  private val atLeastSix = source("opuścisz Polskę na okres co najmniej 6 miesięcy",
    "ви залишите Польщу на строк щонайменше 6 місяців")
  private val aboveSix = source(
    "Jeśli opuścisz Polskę na okres powyżej 6 miesięcy, utracisz zezwolenie na pobyt czasowy.",
    "Якщо ви залишите Польщу на період понад 6 місяців, ви втратите дозвіл на тимчасове проживання.", sheet)
  private val fifteen = source("powinieneś to zrobić w ciągu 15 dni roboczych od dnia zmiany adresu",
    "це потрібно зробити протягом 15 робочих днів від дня зміни адреси")
  private val fourteen = source(
    "masz prawo do złożenia w terminie 14 dni od dnia doręczenia decyzji odwołania do Szefa Urzędu do Spraw Cudzoziemców",
    "ви маєте право протягом 14 днів від дня вручення рішення подати скаргу до Голови Управління у справах іноземців")
  private val replace = source(
    "Będziesz musiał dokonać opłaty 200 zł, a jeśli kolejny raz utracisz kartę pobytu z własnej winy (np. kolejny raz ją zgubisz), opłata wyniesie 300 zł.",
    "Потрібно буде сплатити 200 PLN, а якщо ви вдруге втратите посвідку з власної вини (наприклад знову її загубите), плата становитиме 300 PLN.")

  val reasons: Map[String, Reason] = Map(
    "status_kept" -> Reason(Words("Статус UKR зберігається: після подання заяви можна виїхати не більше ніж на 30 днів.",
      "Статус UKR сохраняется: после подачи заявления можно уехать не более чем на 30 дней.",
      "Status UKR zostaje: po złożeniu wniosku można wyjechać najwyżej na 30 dni.",
      "The UKR status is kept: after applying one may leave for no longer than 30 days."), thirty, good = true),
    "status_lost_no_card" -> Reason(Words(
      "Статус UKR буде втрачено — а без чинного PESEL UKR у день видачі посвідку не видадуть.",
      "Статус UKR будет утрачен — а без действующего PESEL UKR в день выдачи карту не выдадут.",
      "Status UKR zostanie utracony — a bez aktualnego PESEL UKR w dniu wydania karta nie zostanie wydana.",
      "The UKR status will be lost — and without an active PESEL UKR on the day of issue the card is not issued."),
      thirty, Vector(source("Status UKR jest anulowany w przypadku wyjazdu z Polski na okres powyżej 30 dni.",
        "Статус UKR анулюється у разі виїзду з Польщі на строк понад 30 днів."))),
    "collect_it" -> Reason(Words("Посвідку ще можна отримати: на це є 60 днів від повідомлення управління.",
      "Карту ещё можно получить: на это есть 60 дней с уведомления управления.",
      "Kartę można jeszcze odebrać: jest na to 60 dni od zawiadomienia urzędu.",
      "The card can still be collected: there are 60 days from the office's notice."), sixty, good = true),
    "card_annulled" -> Reason(Words("60 днів минуло: посвідку анульовано, сплачені збори не повертаються.",
      "60 дней прошло: карта аннулирована, уплаченные сборы не возвращаются.",
      "Minęło 60 dni: karta została unieważniona, opłaty nie są zwracane.",
      "The 60 days have passed: the card is annulled and the fees are not returned."), sixty),
    "permit_kept" -> Reason(Words("Дозвіл зберігається: відсутність коротша за 6 місяців поспіль.",
      "Разрешение сохраняется: отсутствие короче 6 месяцев подряд.",
      "Zezwolenie zostaje: nieobecność krótsza niż 6 miesięcy z rzędu.",
      "The permit is kept: the absence is shorter than six months at a stretch."), stretch, good = true),
    "permit_may_be_withdrawn" -> Reason(Words("Воєвода може скасувати дозвіл: відсутність довша за 6 місяців поспіль.",
      "Воевода может отозвать разрешение: отсутствие дольше 6 месяцев подряд.",
      "Wojewoda może cofnąć zezwolenie: nieobecność dłuższa niż 6 miesięcy z rzędu.",
      "The voivode may withdraw the permit: the absence is longer than six months at a stretch."),
      stretch, Vector(atLeastSix)),
    "six_months_texts_differ" -> Reason(Words(
      "Рівно 6 місяців: офіційні тексти розходяться («щонайменше 6» і «понад 6»). Вважайте це ризиком і уточніть в управлінні.",
      "Ровно 6 месяцев: официальные тексты расходятся («не менее 6» и «свыше 6»). Считайте это риском и уточните в управлении.",
      "Dokładnie 6 miesięcy: oficjalne teksty różnią się («co najmniej 6» i «powyżej 6»). Potraktuj to jako ryzyko i zapytaj w urzędzie.",
      "Exactly six months: the official texts differ (\"at least 6\" and \"above 6\"). Take it as a risk and ask the office."),
      atLeastSix, Vector(aboveSix)),
    "inform_voivode_in_time" -> Reason(Words(
      "Повідомте воєводу про нову адресу: на це є 15 робочих днів від зміни адреси, строк ще не минув.",
      "Сообщите воеводе новый адрес: на это есть 15 рабочих дней со смены адреса, срок ещё не истёк.",
      "Zawiadom wojewodę o nowym adresie: jest na to 15 dni roboczych od zmiany adresu, termin jeszcze nie minął.",
      "Tell the voivode the new address: there are 15 working days from the change, and they have not passed."),
      fifteen, good = true),
    "inform_voivode_late" -> Reason(Words(
      "15 робочих днів минуло: повідомте воєводу негайно — листи на стару адресу вважатимуться врученими.",
      "15 рабочих дней прошло: сообщите воеводе немедленно — письма на старый адрес будут считаться вручёнными.",
      "Minęło 15 dni roboczych: zawiadom wojewodę niezwłocznie — pisma na stary adres będą uznane za doręczone.",
      "The 15 working days have passed: tell the voivode at once — letters to the old address count as delivered."),
      fifteen, Vector(Source(faq, Map(
        "pl" -> "pisma wojewody będą wysyłane na Twój poprzedni adres, jednak zostaną one uznane za skutecznie doręczone")))),
    "may_appeal" -> Reason(Words("Рішення можна оскаржити: 14 днів від вручення ще не минули.",
      "Решение можно обжаловать: 14 дней со вручения ещё не прошли.",
      "Od decyzji można się odwołać: 14 dni od doręczenia jeszcze nie minęło.",
      "The decision can be appealed: the 14 days from its delivery have not passed."), fourteen, good = true),
    "appeal_time_passed" -> Reason(Words("Строк оскарження — 14 днів від вручення рішення — минув.",
      "Срок обжалования — 14 дней со вручения решения — прошёл.",
      "Termin na odwołanie — 14 dni od doręczenia decyzji — minął.",
      "The time to appeal — 14 days from the decision's delivery — has passed."), fourteen),
    "fee_not_returned" -> Reason(Words("Сплачений гербовий збір не повертається.",
      "Уплаченный гербовый сбор не возвращается.", "Opłata skarbowa nie jest zwracana.",
      "The stamp duty paid is not returned."),
      source("Zwrotu opłaty nie otrzymasz również, gdy postępowanie zostanie umorzone",
        "Повернення збору також не здійснюється, якщо провадження буде припинено")),
    "may_apply_again" -> Reason(Words("Заяву можна подати знову.", "Заявление можно подать снова.",
      "Wniosek można złożyć ponownie.", "The application can be made again."),
      source("będę mógł ponownie złożyć wniosek o wydanie karty pobytu CUKR? Tak.",
        "Чи зможу я подати заяву повторно, якщо отримаю відмову у видачі посвідки на проживання CUKR через відсутність необхідної оплати? Так."),
      good = true),
    "replace_for_200" -> Reason(Words(
      "Якнайшвидше подайте паперову заяву на заміну посвідки воєводі за місцем проживання; плата 200 зл.",
      "Как можно скорее подайте бумажное заявление на замену карты воеводе по месту жительства; плата 200 зл.",
      "Jak najszybciej złóż papierowy wniosek o wymianę karty do wojewody właściwego dla miejsca zamieszkania; opłata 200 zł.",
      "As soon as you can, make a paper application to replace the card to the voivode of your residence; the fee is 200 zł."),
      replace, good = true),
    "replace_for_300" -> Reason(Words(
      "Якнайшвидше подайте паперову заяву на заміну посвідки; за повторну втрату з власної вини плата 300 зл.",
      "Как можно скорее подайте бумажное заявление на замену карты; за повторную утерю по своей вине плата 300 зл.",
      "Jak najszybciej złóż papierowy wniosek o wymianę karty; za kolejną utratę z własnej winy opłata wynosi 300 zł.",
      "As soon as you can, make a paper application to replace the card; lost again by your own fault, the fee is 300 zł."),
      replace, good = true),
    "may_work" -> Reason(Words("Так: з посвідкою CUKR можна працювати у будь-якого роботодавця без дозволу на роботу.",
      "Да: с картой CUKR можно работать у любого работодателя без разрешения на работу.",
      "Tak: z kartą pobytu CUKR można pracować u dowolnego pracodawcy bez zezwolenia na pracę.",
      "Yes: with the CUKR card one may work for any employer with no work permit."),
      source("Tak, z kartą pobytu CUKR masz pełny dostęp do polskiego rynku pracy.",
        "Так, маючи посвідку на проживання CUKR, ви маєте повний доступ до польського ринку праці."), good = true),
    "may_run_business" -> Reason(Words("Так: підприємницьку діяльність можна вести на тих самих умовах, що й громадяни Польщі.",
      "Да: предпринимательскую деятельность можно вести на тех же условиях, что и граждане Польши.",
      "Tak: działalność gospodarczą można prowadzić na takich samych zasadach jak obywatele polscy.",
      "Yes: one may run a business on the same terms as Polish citizens."),
      source("Posiadanie karty pobytu CUKR daje Ci prawo do prowadzenia działalności gospodarczej na takich samych zasadach, jakie obowiązują obywateli polskich.",
        "Наявність посвідки на проживання CUKR надає вам право здійснювати підприємницьку діяльність на тих самих умовах, що й громадянам Польщі."),
      good = true))

  private def two(id: String, text: Words, a: (Words, String), b: (Words, String)) =
    Question(id, text, Vector(Choice("a", a._1, a._2), Choice("b", b._1, b._2)))

  private def topic(model: Model, name: Words, questions: Question*): Topic =
    Topic(name, model, questions.toVector, reasons, Vector.empty, book, fetched)

  /** the situations, each a topic over the same model */
  def topics(model: Model): Vector[Topic] = Vector(
    topic(model, Words("Я подав(ла) заяву — чи можна виїхати з Польщі?", "Я подал(а) заявление — можно ли уехать из Польши?",
      "Złożyłem(-am) wniosek — czy mogę wyjechać z Polski?", "I have applied — may I leave Poland?"),
      two("leave", Words("На скільки днів?", "На сколько дней?", "Na ile dni?", "For how many days?"),
        Words("Не більше ніж на 30 днів", "Не более чем на 30 дней", "Najwyżej na 30 dni", "For 30 days or fewer") ->
          CukrAfter.leaves(30),
        Words("Більше ніж на 30 днів", "Больше чем на 30 дней", "Na dłużej niż 30 dni", "For more than 30 days") ->
          CukrAfter.leaves(31))),
    topic(model, Words("Посвідка готова — до коли її треба отримати?", "Карта готова — до какого срока её нужно получить?",
      "Karta jest gotowa — do kiedy trzeba ją odebrać?", "The card is ready — by when must I collect it?"),
      two("waits", Words("Скільки днів минуло від повідомлення управління?", "Сколько дней прошло с уведомления управления?",
        "Ile dni minęło od zawiadomienia urzędu?", "How many days have passed since the office's notice?"),
        Words("60 днів або менше", "60 дней или меньше", "60 dni lub mniej", "60 days or fewer") -> CukrAfter.waits(60),
        Words("Більше 60 днів", "Больше 60 дней", "Więcej niż 60 dni", "More than 60 days") -> CukrAfter.waits(61))),
    topic(model, Words("Маю посвідку — на скільки можна виїхати з Польщі?", "У меня карта — на сколько можно уехать из Польши?",
      "Mam kartę — na jak długo mogę wyjechać z Polski?", "I hold the card — how long may I be away from Poland?"),
      Question("away", Words("Скільки місяців поспіль триватиме відсутність?", "Сколько месяцев подряд продлится отсутствие?",
        "Ile miesięcy z rzędu potrwa nieobecność?", "How many months at a stretch will the absence last?"), Vector(
        Choice("less", Words("Менше 6 місяців", "Меньше 6 месяцев", "Mniej niż 6 miesięcy", "Less than six months"),
          CukrAfter.away(5)),
        Choice("six", Words("Рівно 6 місяців", "Ровно 6 месяцев", "Dokładnie 6 miesięcy", "Exactly six months"),
          CukrAfter.away(6)),
        Choice("more", Words("Більше 6 місяців", "Больше 6 месяцев", "Więcej niż 6 miesięcy", "More than six months"),
          CukrAfter.away(7))))),
    topic(model, Words("Маю посвідку і змінив(ла) адресу", "У меня карта, и я сменил(а) адрес",
      "Mam kartę i zmieniłem(-am) adres", "I hold the card and I have moved"),
      two("moved", Words("Скільки робочих днів минуло від зміни адреси?", "Сколько рабочих дней прошло со смены адреса?",
        "Ile dni roboczych minęło od zmiany adresu?", "How many working days have passed since the move?"),
        Words("15 робочих днів або менше", "15 рабочих дней или меньше", "15 dni roboczych lub mniej",
          "15 working days or fewer") -> CukrAfter.moved(15),
        Words("Більше 15 робочих днів", "Больше 15 рабочих дней", "Więcej niż 15 dni roboczych",
          "More than 15 working days") -> CukrAfter.moved(16))),
    topic(model, Words("Мені відмовили у видачі посвідки", "Мне отказали в выдаче карты", "Odmówiono mi wydania karty",
      "I was refused the card"),
      two("refused", Words("Скільки днів минуло від вручення рішення?", "Сколько дней прошло со вручения решения?",
        "Ile dni minęło od doręczenia decyzji?", "How many days have passed since the decision was delivered?"),
        Words("14 днів або менше", "14 дней или меньше", "14 dni lub mniej", "14 days or fewer") -> CukrAfter.refused(14),
        Words("Більше 14 днів", "Больше 14 дней", "Więcej niż 14 dni", "More than 14 days") -> CukrAfter.refused(15)),
      two("unpaid", Words("Чому відмовили?", "Почему отказали?", "Z jakiego powodu odmówiono?", "Why was it refused?"),
        Words("Через несплачений збір", "Из-за неуплаченного сбора", "Z powodu braku opłaty", "For a fee not paid") ->
          CukrAfter.unpaid(true),
        Words("З іншої причини", "По другой причине", "Z innego powodu", "For another reason") ->
          CukrAfter.unpaid(false))),
    topic(model, Words("Я загубив(ла) посвідку", "Я потерял(а) карту", "Zgubiłem(-am) kartę", "I have lost the card"),
      two("lost", Words("Це вперше?", "Это впервые?", "Czy to pierwszy raz?", "Is it the first time?"),
        Words("Так, уперше", "Да, впервые", "Tak, pierwszy raz", "Yes, the first time") -> CukrAfter.lost(false),
        Words("Ні, знову і з власної вини", "Нет, снова и по своей вине", "Nie, kolejny raz i z własnej winy",
          "No, again and by my own fault") -> CukrAfter.lost(true))),
    topic(model, Words("Маю посвідку — робота і власна справа", "У меня карта — работа и своё дело",
      "Mam kartę — praca i własna firma", "I hold the card — work and a business"),
      two("wants", Words("Що вас цікавить?", "Что вас интересует?", "Co Cię interesuje?", "What do you want to do?"),
        Words("Працювати за наймом", "Работать по найму", "Pracować u pracodawcy", "To work for an employer") ->
          CukrAfter.work,
        Words("Відкрити власну справу", "Открыть своё дело", "Założyć własną firmę", "To run a business") ->
          CukrAfter.business)))
