/** okay-migrant: a legal assistant for an immigrant — a country's rules read from its official texts into exact
 * models (okay-study), the assistant asks for the facts a case lacks and answers with the source and its date */
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

ThisBuild / organization := "io.github.okay-effects"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.8"
ThisBuild / scalacOptions ++= Seq("-deprecation", "-feature", "-Wunused:all", "-Werror")
ThisBuild / resolvers ++= Seq("okay-datalog" at "https://okay-effects.github.io/okay-datalog/maven",
  "okay-llm" at "https://okay-effects.github.io/okay-llm/maven",
  "okay-scala" at "https://okay-effects.github.io/okay-scala/maven",
  "okay-simple" at "https://okay-effects.github.io/okay-simple/maven",
  "okay-faces" at "https://okay-effects.github.io/okay-faces/maven",
  "okay-http" at "https://okay-effects.github.io/okay-http/maven",
  "okay-ui" at "https://okay-effects.github.io/okay-ui/maven")

val munit = Def.setting("org.scalameta" %%% "munit" % "1.2.3" % Test)
val study = "0.1.0-SNAPSHOT" // ivy-local for now: `sbt coreJVM/publishLocal coreJS/publishLocal study/publishLocal` there

/** the topics' cases, readings and questions, the conversation (the JVM and Scala.js); on the JVM also the books
 * written for a teacher, the console, the tests. `Models`: the checked models as text, made from `models/` */
lazy val core = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Full).in(file("core"))
  .settings(name := "okay-migrant",
    libraryDependencies ++= Seq("io.github.okay-effects" %%% "okay-study" % study,
      "io.github.okay-effects" %%% "okay-scala" % "0.1.1"),
    Compile / sourceGenerators += Def.task {
      val models = (ThisBuild / baseDirectory).value / "models"
      def text(file: String): String = {
        val json = IO.read(models / file)
        require(!json.contains("\"\"\""), s"$file: a model with a triple quote in it")
        "\"\"\"" + json + "\"\"\""
      }
      val out = (Compile / sourceManaged).value / "okay" / "migrant" / "Models.scala"
      IO.write(out, "package okay.migrant\n\n/** the checked models as text (made from `models/` by the build) */\n" +
        "object Models:\n  val plCukr: String = " + text("pl-cukr.json") + "\n  val plCukrAfter: String = " +
        text("pl-cukr-after.json") + "\n")
      Seq(out)
    }.taskValue)
  .jvmSettings(libraryDependencies ++= Seq("io.github.okay-effects" %% "okay-study-teacher" % study, munit.value),
    Test / fork := true, Test / baseDirectory := (ThisBuild / baseDirectory).value)

/** the page's program (okay-ui), played by a script on the JVM and run by the Laminar face in the browser */
lazy val app = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("app")).dependsOn(core)
  .settings(name := "okay-migrant-app", publish / skip := true,
    libraryDependencies ++= Seq("io.github.okay-effects" %%% "okay-ui" % "0.1.0", munit.value))
  .jsSettings(
    Compile / unmanagedSourceDirectories += (ThisBuild / baseDirectory).value / "app" / "src" / "main" / "scala-js",
    scalaJSUseMainModuleInitializer := true,
    Compile / mainClass := Some("okay.migrant.app.InTheBrowser"),
    Compile / fullLinkJS / scalaJSLinkerConfig ~= (_.withClosureCompiler(true)))

/** THE PAGE: index.html and main.js (optimised), in app/js/target/web */
val web = taskKey[File]("the page: index.html and main.js (optimised), in target/web")
lazy val appWeb = Def.task {
  val base = (ThisBuild / baseDirectory).value
  val out = (app.js / target).value / "web"
  val linked = (app.js / Compile / fullLinkJS).value.data.publicModules.head.jsFileName
  val from = (app.js / Compile / fullLinkJS / scalaJSLinkerOutputDirectory).value / linked
  IO.delete(out)
  IO.copyFile(base / "app" / "web" / "index.html", out / "index.html")
  IO.copyFile(from, out / "main.js")
  out
}

/** PUBLISHING THE PAGE: `sbt publishSite` — the page built by `web` from a CLEAN tree, pushed to the public
 * repository of the built page (served by GitHub Pages) as one commit naming this repository's;
 * `-Dsite.repo=<git url>` for another. The source never leaves this repository */
val publishSite = taskKey[Unit]("build the page and push it to the site's public repository")
val siteRepo = sys.props.getOrElse("site.repo", "https://github.com/okay-effects/okay-migrant.git")
lazy val appPublishSite = Def.taskDyn {
  import scala.sys.process._
  val base = (ThisBuild / baseDirectory).value
  val dirty = Process(Seq("git", "status", "--porcelain"), base).!!.trim
  if (dirty.nonEmpty)
    sys.error(s"publishSite: the working tree has changes not committed — commit or stash them first:\n$dirty")
  Def.task {
    val log = streams.value.log
    val page = web.value
    val sha = Process(Seq("git", "rev-parse", "--short", "HEAD"), base).!!.trim
    val repo = target.value / "site-repo"
    def git(args: String*): Unit =
      if (Process("git" +: args, repo).! != 0) sys.error(s"publishSite: git ${args.mkString(" ")} failed")
    if (!(repo / ".git").exists) {
      IO.delete(repo)
      if (Process(Seq("git", "clone", "--depth", "1", siteRepo, repo.getPath)).! != 0)
        sys.error(s"publishSite: could not clone $siteRepo")
    }
    Process(Seq("git", "fetch", "-q", "--depth", "1", "origin"), repo).! // fails only while the site is empty
    if (Process(Seq("git", "rev-parse", "--verify", "-q", "origin/main"), repo).! == 0)
      git("checkout", "-q", "-B", "main", "origin/main")
    else git("checkout", "-q", "--orphan", "main")
    IO.listFiles(repo).filterNot(_.getName == ".git").foreach(IO.delete)
    IO.copyDirectory(page, repo)
    IO.touch(repo / ".nojekyll")
    (repo ** "*.map").get.foreach(IO.delete)
    git("add", "-A")
    if (Process(Seq("git", "diff", "--cached", "--quiet"), repo).! == 0) log.info("publishSite: nothing changed")
    else {
      git("commit", "-q", "-m", s"okay-migrant at $sha")
      git("push", "-q", "origin", "main")
      log.info(s"publishSite: okay-migrant at $sha pushed to $siteRepo")
    }
  }
}

lazy val root = (project in file(".")).aggregate(core.jvm, core.js, app.jvm, app.js)
  .settings(publish / skip := true, web := appWeb.value, publishSite := appPublishSite.value)
