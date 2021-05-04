import sbt._

object Dependencies {
  object Logging {
    private val logbackVersion      = "1.2.3"
    private val scalaLoggingVersion = "3.9.2"
    val logback                     = "ch.qos.logback" % "logback-classic" % logbackVersion
    val scalaLogging                = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  }

  object PureConfig {
    private val pureConfigVersion = "0.15.0"
    val pureconfig                = "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
  }

  object Http4s {
    private val http4sVersion = "0.21.22"
    val dsl                   = "org.http4s" %% "http4s-dsl" % http4sVersion
    val blazeServer           = "org.http4s" %% "http4s-blaze-server" % http4sVersion
    val circe                 = "org.http4s" %% "http4s-circe" % http4sVersion
  }

  object Circe {
    private val circeVersion = "0.13.0"
    val core                 = "io.circe" %% "circe-core" % circeVersion
    val generic              = "io.circe" %% "circe-generic" % circeVersion
    val parser               = "io.circe" %% "circe-parser" % circeVersion
  }

  object Doobie {
    private val doobieVersion = "0.12.1"
    val core                  = "org.tpolecat" %% "doobie-core" % doobieVersion
    val postgres              = "org.tpolecat" %% "doobie-postgres" % doobieVersion
    val postgesqlCirce        = "org.tpolecat" %% "doobie-postgres-circe" % doobieVersion
    val hikari                = "org.tpolecat" %% "doobie-hikari" % doobieVersion
  }

  object Flyway {
    private val flywayVersion = "7.8.2"
    val core                  = "org.flywaydb" % "flyway-core" % flywayVersion
  }

  object Scalatest {
    private val scalaTestVersion = "3.0.5"
    val scalaTest                = "org.scalatest" %% "scalatest" % scalaTestVersion % "it, test"
  }
}
