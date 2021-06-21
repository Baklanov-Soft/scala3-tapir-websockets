import sbt._

object Dependencies {

  object Versions {
    lazy val tapir = "0.18.0-M17"
  }

  lazy val tapir = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core",
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe",
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"
  ).map(_ % Versions.tapir)

}
