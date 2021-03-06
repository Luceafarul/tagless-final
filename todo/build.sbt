import Dependencies._
import Util._

ThisBuild / organization := "com.devinsideyou"
ThisBuild / scalaVersion := "2.13.2"
ThisBuild / version := "0.0.1-SNAPSHOT"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked",
  "Wvalue-discard",
  "-Xfatal-warnings",
  "-Ymacro-annotations"
)

lazy val `todo` =
  project
    .in(file("."))
    .aggregate(
      `handmade-cats-core`,
      `handmade-cats-effect`,
      domain,
      core,
      delivery,
      persistence,
      main
    )

lazy val `handmade-cats-core` =
  project
    .in(file("00-handmade-cats-core"))

lazy val `handmade-cats-effect` =
  project
    .dependsOn(`handmade-cats-core` % Cctt)
    .in(file("00-handmade-cats-effect"))

lazy val domain =
  project
    .in(file("01-domain"))

lazy val core =
  project
    .in(file("02-core"))
    .dependsOn(`handmade-cats-core` % Cctt)
    .dependsOn(domain % Cctt)
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        com.github.alexarchambault.`scalacheck-shapeless_1.14`,
        org.scalacheck.scalacheck,
        org.scalatest.scalatest,
        org.scalatestplus.`scalatestplus-scalacheck`
      ).map(_ % Test)
    )

lazy val delivery =
  project
    .in(file("03-delivery"))
    .dependsOn(core % Cctt)
    .dependsOn(`handmade-cats-effect` % Cctt)
    .settings(commonSettings: _*)

lazy val persistence =
  project
    .in(file("03-persistence"))
    .dependsOn(`handmade-cats-core` % Cctt)
    .dependsOn(`handmade-cats-effect` % Cctt)
    .dependsOn(core % Cctt)
    .settings(commonSettings: _*)

lazy val main =
  project
    .in(file("04-main"))
    .dependsOn(delivery % Cctt)
    .dependsOn(persistence % Cctt)
    .settings(commonSettings: _*)

lazy val commonSettings = Seq(
  addCompilerPlugin(org.typelevel.`kind-projector`),
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings"
  ),
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value
)
