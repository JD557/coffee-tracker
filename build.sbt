import scala.sys.process._
import scala.language.postfixOps

import sbtwelcome._

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"

lazy val coffeetracker =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin)
    .settings( // Normal settings
      name         := "coffeetracker",
      version      := "0.0.1",
      scalaVersion := "3.4.1",
      organization := "eu.joaocosta",
      libraryDependencies ++= Seq(
        "io.indigoengine"   %%% "tyrian-io"          % "0.11.0",
        "io.github.cquiroz" %%% "scala-java-time"    % "2.6.0",
        "io.github.cquiroz" %%% "scala-java-locales" % "1.5.4",
        "eu.joaocosta"      %%% "minart"             % "0.6.1-SNAPSHOT",
        "org.scalameta"     %%% "munit"              % "0.7.29" % Test
      ),
      testFrameworks += new TestFramework("munit.Framework"),
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
      scalafixOnCompile := true,
      semanticdbEnabled := true,
      semanticdbVersion := scalafixSemanticdb.revision,
      autoAPIMappings   := true
    )
    .settings( // Launch VSCode when you type `code` in the sbt terminal
      code := {
        val command = Seq("code", ".")
        val run = sys.props("os.name").toLowerCase match {
          case x if x contains "windows" => Seq("cmd", "/C") ++ command
          case _                         => command
        }
        run.!
      }
    )
    .settings( // Welcome message
      logo := List(
        "",
        "Coffee Tracker (v" + version.value + ")",
        "",
        "> Please Note: By default tyrianapp.js expects you to run fastLinkJS.",
        ">              To use fullOptJS, edit tyrianapp.js replacing '-fastopt'",
        ">              with '-opt'.",
        ""
      ).mkString("\n"),
      usefulTasks := Seq(
        UsefulTask(
          "fastLinkJS",
          "Rebuild the JS (use during development)"
        ).noAlias,
        UsefulTask(
          "fullLinkJS",
          "Rebuild the JS and optimise (use in production)"
        ).noAlias,
        UsefulTask("code", "Launch VSCode").noAlias
      ),
      logoColor        := scala.Console.MAGENTA,
      aliasColor       := scala.Console.BLUE,
      commandColor     := scala.Console.CYAN,
      descriptionColor := scala.Console.WHITE
    )

lazy val code =
  taskKey[Unit]("Launch VSCode in the current directory")
