import sbt._

object Dependencies {

  object Versions {
    lazy val cats       = "2.6.1"
    lazy val catsEffect = "2.5.1"
    lazy val circe      = "0.14.1"
    lazy val fs2        = "2.5.6"
    lazy val http4s     = "0.22.0-RC1"
    lazy val tapir      = "0.18.0-M17"
    lazy val logback    = "1.2.3"
  }

  lazy val tapir = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core",
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe",
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml",
    "com.softwaremill.sttp.tapir" %% "tapir-asyncapi-circe-yaml"
  ).map(_ % Versions.tapir) ++
    List(
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s",
      "com.softwaremill.sttp.tapir" %% "tapir-asyncapi-docs"
    ).map(_ % Versions.tapir)
      .map(_.cross(CrossVersion.for3Use2_13))

  lazy val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % Versions.circe)

  lazy val fs2   = Seq(
    "co.fs2" %% "fs2-core"
  ).map(_ % Versions.fs2)

  lazy val cats  = Seq(
    "org.typelevel" %% "cats-core"   % Versions.cats,
    "org.typelevel" %% "cats-effect" % Versions.catsEffect
  )

  lazy val http4s  = Seq(
    "org.http4s" %% "http4s-core",
    "org.http4s" %% "http4s-server",
    "org.http4s" %% "http4s-blaze-core",
    "org.http4s" %% "http4s-blaze-server"
  ).map(_ % Versions.http4s)

  lazy val logback = "ch.qos.logback" % "logback-classic" % Versions.logback

}
