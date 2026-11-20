package okay.migrant.app

import okay.business.faces.Skin
import okay.business.laminar.Laminar
import okay.business.model.Ui
import okay.scala3.*
import okay.scala3.std.*
import org.scalajs.dom

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

/** the page: the assistant's program run by okay-ui's Laminar face; everything stays in the browser — no request
 * leaves it but the page's own files. The program's address is the URL's fragment: each screen a new entry of the
 * browser's history, so a link to it can be sent and the browser's Back goes to the screen before */
object InTheBrowser:
  type Browser[X] = (Async + Ui + Address)[X]
  type Shown[X] = (Async + Ui)[X]
  val skin: Skin = Skin.calm(accent = "#1f5fbf", deep = "#12366e")

  /** the header's button: the link to this screen, sent by the phone's own sharing, or copied; its words in the
   * screen's language */
  object Share:
    private val words = Map(
      "uk" -> ("Поділитися посиланням", "Посилання скопійовано"), "ru" -> ("Поделиться ссылкой", "Ссылка скопирована"),
      "pl" -> ("Udostępnij link", "Link skopiowany"), "en" -> ("Share link", "Link copied"))
    private def button = Option(dom.document.getElementById("share"))
    private def said(path: Vector[String]) = words.getOrElse(path.headOption.getOrElse("en"), words("en"))

    /** the foot's note in the screen's language (English before a language is chosen) */
    private val notes = Map(
      "uk" -> ("Інформація з офіційних матеріалів польських установ (UdSC, MSWiA, Управа міста Варшави), а не " +
        "юридична консультація. Ваші відповіді залишаються на цій сторінці: нічого нікуди не надсилається. Посилання " +
        "на екран містить відповіді, що до нього ведуть: надсилайте його лише тим, кому хочете їх показати."),
      "ru" -> ("Информация из официальных материалов польских учреждений (UdSC, MSWiA, Управа города Варшавы), а не " +
        "юридическая консультация. Ваши ответы остаются на этой странице: ничего никуда не отправляется. Ссылка на " +
        "экран содержит ответы, которые к нему ведут: отправляйте её только тем, кому хотите их показать."),
      "pl" -> ("Informacje z oficjalnych materiałów polskich urzędów (UdSC, MSWiA, Urząd m.st. Warszawy), " +
        "a nie porada " +
        "prawna. Twoje odpowiedzi zostają na tej stronie: nic nie jest nigdzie wysyłane. Link do ekranu zawiera " +
        "odpowiedzi, które do niego prowadzą: wysyłaj go tylko tym, którym chcesz je pokazać."))
    private lazy val english = Option(dom.document.getElementById("note")).map(_.textContent).getOrElse("")

    def at(path: Vector[String]): Unit =
      button.foreach(_.textContent = said(path)._1)
      val note = english
      Option(dom.document.getElementById("note")).foreach(_.textContent =
        path.headOption.flatMap(notes.get).getOrElse(note))
      path.headOption.foreach(l => dom.document.documentElement.setAttribute("lang", l))

    def install(): Unit = button.foreach { b =>
      at(Address.path(dom.window.location.hash))
      b.addEventListener("click", (_: dom.Event) =>
        val url = dom.window.location.href
        val navigator = dom.window.navigator.asInstanceOf[js.Dynamic]
        if !js.isUndefined(navigator.share) then
          navigator.share(js.Dynamic.literal(title = dom.document.title, url = url))
        else
          dom.window.navigator.clipboard.writeText(url)
          val path = Address.path(dom.window.location.hash)
          b.textContent = said(path)._2
          js.timers.setTimeout(2000)(at(Address.path(dom.window.location.hash))))
    }

  /** the address in the URL: opened at the fragment; a new place pushed as a new entry of the history */
  def inTheUrl[A](p: A ! Browser): A ! Shown =
    handle(p, Handler.tail[Address, Shown, A, A](pure)([X] => (op: Address[X]) => op match
      case Address.Opened => Address.path(dom.window.location.hash)
      case Address.Now(path) =>
        Share.at(path)
        val fragment = Address.fragment(path)
        if dom.window.location.hash != fragment then
          dom.window.history.pushState(null, "", if fragment.isEmpty then dom.window.location.pathname else fragment)
        ()))

  def main(args: Array[String]): Unit =
    val root = Option(dom.document.getElementById("app")).getOrElse(dom.document.body)
    val now = new js.Date()
    val today = now.getFullYear().toInt * 10000 + (now.getMonth().toInt + 1) * 100 + now.getDate().toInt
    def start() =
      val running = Async.start(Laminar.run(root, skin)(inTheUrl(Assistant.main[Browser](today))))
      running.future.failed.foreach { case _: Cancelled => (); case e => dom.console.error(e.toString) }
      running
    Share.install()
    var running = start()
    // Back, Forward, or a link opened in this page: the program again, from that address
    dom.window.addEventListener("popstate", (_: dom.Event) => { running.cancel(); running = start() })
    dom.window.addEventListener("pagehide", (_: dom.Event) => running.cancel())
