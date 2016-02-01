import sbt._
import Keys._

import wartremover._

object JsonZeroBuild extends Build {
  private val buildSettings = Defaults.coreDefaultSettings ++ Seq(
    organization  := "com.dslplatform.json"
  , version       := "0.0.0"
  , scalaVersion  := "2.11.7"

  , scalacOptions ++= Seq(
      "-deprecation"
    , "-encoding", "UTF-8"
    , "-feature"
    , "-language:_"
    , "-target:jvm-1.8"
    , "-unchecked"
    , "-Xlint"
    , "-Yclosure-elim"
    , "-Yconst-opt"
    , "-Ydead-code"
    , "-Ywarn-adapted-args"
    , "-Ywarn-dead-code"
    , "-Ywarn-inaccessible"
    , "-Ywarn-infer-any"
    , "-Ywarn-nullary-override"
    , "-Ywarn-nullary-unit"
    , "-Ywarn-numeric-widen"
    , "-Ywarn-unused"
    , "-Xlog-free-terms"
    )

  , wartremoverWarnings in (Compile, compile) ++= Seq(
      Wart.Product
    , Wart.Serializable
    , Wart.TryPartial
    )

  , wartremoverErrors in (Compile, compile) ++= Seq(
      Wart.Any2StringAdd
    , Wart.EitherProjectionPartial
    , Wart.Enumeration
    , Wart.JavaConversions
    , Wart.Option2Iterable
    , Wart.Return
    )
  )

  lazy val macros = (project
    settings(buildSettings)
    settings(
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value
      , "com.softwaremill.scalamacrodebug" %% "macros" % "0.4"
      , "com.dslplatform" % "dsl-json" % "0.9.5"
      )
    )
  )

  import pl.project13.scala.sbt.JmhPlugin

  lazy val testing = (project
    settings(buildSettings)
    settings(
      libraryDependencies ++= Seq(
        "com.fasterxml.jackson.module" %% "jackson-module-scala"       % "2.6.3"
      , "com.fasterxml.jackson.module" %  "jackson-module-afterburner" % "2.6.3"
      , "org.specs2" %% "specs2-scalacheck" % "3.7" % "test"
      )
    )
    enablePlugins(JmhPlugin)
    dependsOn(macros)
  )

  lazy val root = (project
    in(file("."))
    settings(buildSettings)
    aggregate(macros, testing)
  )
}
