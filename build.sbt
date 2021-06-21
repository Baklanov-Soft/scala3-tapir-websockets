inThisBuild(semanticdbEnabled := true)

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-tapir-websockets",
    version := "0.1.0",
    scalaVersion := "3.0.0",
    libraryDependencies ++= Dependencies.tapir,
    libraryDependencies ++= Dependencies.circe,
    libraryDependencies ++= Dependencies.fs2,
    libraryDependencies ++= Dependencies.cats,
    libraryDependencies += Dependencies.logback,
    libraryDependencies ++= Dependencies.http4s,
    dependencyOverrides ++= Dependencies.http4s
  )
