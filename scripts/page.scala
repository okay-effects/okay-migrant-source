//> using scala 3.3.8
//> using dep com.microsoft.playwright:playwright:1.62.0

import com.microsoft.playwright.{Browser, BrowserType, Page, Playwright}
import com.sun.net.httpserver.{HttpExchange, HttpServer}

import java.net.InetSocketAddress
import java.nio.file.{Files, Path}
import scala.jdk.CollectionConverters.*

/** THE PAGE DRIVEN AS A PERSON WOULD, at a phone's size: the built page served from its directory, a language
 * chosen, the questions answered by their buttons, the answer read; a screenshot of the answer. The analytics
 * beacon is answered with an empty script (a test is not a visit).
 * `scala-cli run scripts/page.scala -- <web dir | https://the live page/> <out.png>` */
@main def page(dir: String, out: String): Unit =
  val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
  server.createContext("/", (x: HttpExchange) =>
    val name = x.getRequestURI.getPath.stripPrefix("/") match { case "" => "index.html"; case n => n }
    val file = Path.of(dir, name)
    if Files.isRegularFile(file) then
      val bytes = Files.readAllBytes(file)
      x.getResponseHeaders.add("Content-Type", if name.endsWith(".js") then "text/javascript" else "text/html; charset=utf-8")
      x.sendResponseHeaders(200, bytes.length); x.getResponseBody.write(bytes)
    else x.sendResponseHeaders(404, -1)
    x.close())
  server.start()
  val url = if dir.startsWith("http") then dir else s"http://127.0.0.1:${server.getAddress.getPort}/"
  val exe = sys.env.getOrElse("CHROME", sys.props("user.home") +
    "/Library/Caches/ms-playwright/chromium-1234/chrome-mac-arm64/" +
    "Google Chrome for Testing.app/Contents/MacOS/Google Chrome for Testing")
  val pw = Playwright.create(Playwright.CreateOptions().setEnv(Map("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD" -> "1").asJava))
  val browser = pw.chromium().launch(BrowserType.LaunchOptions().setExecutablePath(Path.of(exe)).setHeadless(true))
  try
    val ctx = browser.newContext(Browser.NewContextOptions().setViewportSize(390, 844).setDeviceScaleFactor(2))
    ctx.route("**/beacon.min.js", r => r.fulfill(com.microsoft.playwright.Route.FulfillOptions().setStatus(200)
      .setContentType("text/javascript").setBody("")))
    val p = ctx.newPage()
    var failed = Vector.empty[String]
    p.onConsoleMessage(m => if m.`type`() == "error" then failed :+= "console: " + m.text().take(300))
    p.onPageError(e => failed :+= "page error: " + e.take(300))
    p.onRequest(r => if !r.url().startsWith(url) && !r.url().contains("cloudflareinsights") then failed :+= "a request left the page: " + r.url())
    p.setDefaultTimeout(15000)
    p.navigate(url)
    def press(label: String): Unit = p.click(s"button:text-is(\"$label\")")
    def body(): String = p.locator("body").textContent().replaceAll("\\s+", " ")
    // in Russian: a person who came, PESEL yes, 4 June yes, under a year, today, registers complete
    press("Русский")
    press("Карта CUKR (для граждан Украины со статусом UKR)")
    press("Могу ли я подать заявление на карту CUKR?")
    press("Для человека, который приехал в Польшу из Украины после 23 февраля 2022 г.")
    press("Да"); p.waitForSelector("text=4 июня 2025"); press("Да")
    press("Меньше 365 дней, или был перерыв (периоды не складываются)")
    press("Сегодня"); p.waitForSelector("text=MOS"); press("Да")
    p.waitForSelector("text=Сейчас подать не получится")
    val refused = body()
    require(refused.contains("posiadałeś status UKR nieprzerwanie przez co najmniej 365 dni"), refused.take(600))
    require(refused.contains("не юридическая консультация") && refused.contains("2026-11-19"), refused.take(600))
    p.screenshot(Page.ScreenshotOptions().setPath(Path.of(out).resolveSibling("refused.png")).setFullPage(true))
    // again, in Ukrainian: a child born in Poland, the mother with the card
    press("Ещё раз")
    press("Українська")
    press("Посвідка CUKR (для громадян України зі статусом UKR)")
    press("Чи можу я подати заяву на посвідку CUKR?")
    press("Для дитини, народженої в Польщі після 23 лютого 2022 р.")
    press("Так"); p.waitForSelector("text=мати"); press("Так")
    press("Сьогодні"); p.waitForSelector("text=MOS"); press("Так")
    p.waitForSelector("text=Можна подавати")
    val allowed = body()
    require(allowed.contains("у день видачі воєводою посвідки на проживання CUKR"), allowed.take(600))
    require(allowed.contains("Built on") && allowed.contains("Need more?"), "the foot")
    p.screenshot(Page.ScreenshotOptions().setPath(Path.of(out)).setFullPage(true))
    // what follows, in Polish: the card held, away for exactly six months — the page says the texts differ
    press("Ще раз")
    press("Polski")
    press("Karta pobytu CUKR (dla obywateli Ukrainy ze statusem UKR)")
    press("Mam kartę — na jak długo mogę wyjechać z Polski?")
    press("Dokładnie 6 miesięcy")
    p.waitForSelector("text=oficjalne teksty różnią się")
    val six = body()
    require(six.contains("co najmniej 6 miesięcy") && six.contains("powyżej 6 miesięcy"), six.take(700))
    p.screenshot(Page.ScreenshotOptions().setPath(Path.of(out).resolveSibling("six.png")).setFullPage(true))
    // in English: refused for a fee not paid, within the fourteen days
    press("Jeszcze raz")
    press("English")
    press("The CUKR card (for Ukrainians with the UKR status)")
    press("I was refused the card")
    press("14 days or fewer"); press("For a fee not paid")
    p.waitForSelector("text=The application can be made again")
    val refusal = body()
    require(refusal.contains("can be appealed") && refusal.contains("not returned"), refusal.take(700))
    // permanent residence, in Ukrainian: a spouse married for two years without the two years on the permit
    press("Again")
    press("Українська")
    press("Дозвіл на постійне проживання (pobyt stały)")
    press("Я у шлюбі з громадянином / громадянкою Польщі")
    press("Менше 3 років"); press("Ні")
    p.waitForSelector("text=За цією підставою — ні")
    val spouse = body()
    require(spouse.contains("перебуває у шлюбі щонайменше 3 роки") && spouse.contains("pobyt czasowy"), spouse.take(700))
    p.screenshot(Page.ScreenshotOptions().setPath(Path.of(out).resolveSibling("spouse.png")).setFullPage(true))
    // the EU resident, in Russian: one absence of more than six months
    press("Ще раз")
    press("Русский")
    press("Разрешение долгосрочного резидента ЕС")
    press("Работа, бизнес, семья, постоянное разрешение или другое временное разрешение")
    press("5 лет или больше"); press("Больше 6 месяцев"); press("10 месяцев или меньше")
    press("Да"); press("Да"); press("Есть документ о B1 или выше (сертификат, аттестат польской школы и т. п.)")
    p.waitForSelector("text=Сейчас — нет")
    val absent = body()
    require(absent.contains("не превышал 6 месяцев единовременно"), absent.take(700))
    p.screenshot(Page.ScreenshotOptions().setPath(Path.of(out).resolveSibling("eu.png")).setFullPage(true))
    require(failed.isEmpty, failed.mkString("\n"))
    println(s"the page: six conversations answered as expected, no error, no request beyond the page; $out")
  finally { browser.close(); pw.close(); server.stop(0) }
