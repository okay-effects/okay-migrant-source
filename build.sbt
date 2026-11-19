/** okay-migrant: a legal assistant for an immigrant — a country's rules read from its official texts into exact
 * models (okay-study), the assistant asks for the facts a case lacks and answers with the source and its date */
ThisBuild / organization := "io.github.okay-effects"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.8"
ThisBuild / scalacOptions ++= Seq("-deprecation", "-feature", "-Wunused:all", "-Werror")
ThisBuild / resolvers ++= Seq("okay-datalog" at "https://okay-effects.github.io/okay-datalog/maven",
  "okay-llm" at "https://okay-effects.github.io/okay-llm/maven",
  "okay-scala" at "https://okay-effects.github.io/okay-scala/maven",
  "okay-simple" at "https://okay-effects.github.io/okay-simple/maven")

/** the books' cases and reference readings, the assistant's programs; okay-study (ivy-local for now: `sbt
 * core/publishLocal study/publishLocal` in ~/work/my/okay-study) gives the model, its exact run and the study */
lazy val core = (project in file("core")).settings(name := "okay-migrant",
  libraryDependencies ++= Seq("io.github.okay-effects" %% "okay-study" % "0.1.0-SNAPSHOT",
    "io.github.okay-effects" %% "okay-study-teacher" % "0.1.0-SNAPSHOT",
    "org.scalameta" %% "munit" % "1.2.3" % Test),
  Test / fork := true,
  Test / baseDirectory := (ThisBuild / baseDirectory).value)

lazy val root = (project in file(".")).aggregate(core).settings(publish / skip := true)

