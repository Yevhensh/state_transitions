import Dependencies._

name := "state_transitions"

version := "0.1"

lazy val settings = Seq(
  name := "state-transitions",
  scalaVersion := "2.13.5",
  libraryDependencies ++= Seq(
    PureConfig.pureconfig,
    Http4s.dsl,
    Http4s.circe,
    Http4s.blazeServer,
    Circe.core,
    Circe.generic,
    Circe.parser,
    Logging.scalaLogging,
    Logging.logback,
    Doobie.core,
    Doobie.hikari,
    Doobie.postgres,
    Doobie.postgesqlCirce,
    Flyway.core
  )
)

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, DockerPlugin, BuildInfoPlugin)
  .settings(settings)
