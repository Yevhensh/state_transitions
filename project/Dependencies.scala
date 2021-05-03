import sbt._

object Dependencies {
  object PureConfig {
    private val pureConfigVersion = "0.15.1"
    val pureconfig                = "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
  }

  object Http4s {
    private val http4sVersion = "0.21.22"
    val dsl                   = "org.http4s" %% "http4s-dsl" % http4sVersion
    val blazeServer           = "org.http4s" %% "http4s-blaze-server" % http4sVersion
    val circe                 = "org.http4s" %% "http4s-circe" % http4sVersion
  }
}
