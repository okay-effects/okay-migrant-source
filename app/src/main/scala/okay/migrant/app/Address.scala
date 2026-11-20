package okay.migrant.app

import okay.scala3.*

/** THE PAGE'S ADDRESS, as the program sees it: where it was opened (a path: the language, the area, the topic, the
 * answers given) and where it is now. A link sent to someone is that path: opened, the program goes straight to
 * the same screen. What an address is — a page's URL, a test's list — is the handler's */
enum Address[X]:
  case Opened extends Address[Vector[String]]
  case Now(path: Vector[String]) extends Address[Unit]

object Address:
  def opened[R[_]](using In[Address, R]): Vector[String] ! R = perform[Address](Opened)
  def now[R[_]](path: Vector[String])(using In[Address, R]): Unit ! R = perform[Address](Now(path))

  /** a path as a URL's fragment, `#/ru/pesel/how-to-get-pesel-ukr/who=ua`, and back */
  def fragment(path: Vector[String]): String = if path.isEmpty then "" else path.mkString("#/", "/", "")
  def path(fragment: String): Vector[String] =
    fragment.stripPrefix("#").split('/').toVector.filter(_.nonEmpty)

  /** an address played: opened at `path`, every place the program goes to kept in `went` */
  def played[R[_], Rest[_], A](path: Vector[String], went: collection.mutable.Buffer[Vector[String]])(p: A ! R)(
      using Split[Address, R, Rest]): A ! Rest =
    handle(p, Handler.tail[Address, Rest, A, A](pure)([X] => (op: Address[X]) => op match
      case Opened => path
      case Now(at) => went += at; ()))
