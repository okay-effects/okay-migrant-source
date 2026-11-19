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
 * leaves it but the page's own files */
object InTheBrowser:
  type Browser[X] = (Async + Ui)[X]
  val skin: Skin = Skin.calm(accent = "#1f5fbf", deep = "#12366e")

  def main(args: Array[String]): Unit =
    val root = Option(dom.document.getElementById("app")).getOrElse(dom.document.body)
    val now = new js.Date()
    val today = now.getFullYear().toInt * 10000 + (now.getMonth().toInt + 1) * 100 + now.getDate().toInt
    val running = Async.start(Laminar.run(root, skin)(Assistant.main[Browser](today)))
    running.future.failed.foreach { case _: Cancelled => (); case e => dom.console.error(e.toString) }
    dom.window.addEventListener("pagehide", (_: dom.Event) => running.cancel())
