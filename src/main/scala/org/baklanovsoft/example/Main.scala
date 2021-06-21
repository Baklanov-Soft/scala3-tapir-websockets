package org.baklanovsoft.example

import cats.implicits._

import scala.concurrent.ExecutionContext
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.ContextShift
import cats.effect.ExitCode
import org.baklanovsoft.example.endpoints.TestEndpoints

import sttp.tapir._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.effect.IO
import org.http4s.HttpRoutes
import cats.effect.{ContextShift, Timer}
import sttp.tapir.swagger.http4s.SwaggerHttp4s

import sttp.tapir._
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.circe.yaml._
import cats.effect.Blocker
import sttp.model.StatusCode
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.syntax.all._
import sttp.tapir.server.http4s._
import cats.effect.Resource
import org.http4s.server.Server

import scala.concurrent.duration

object Main extends IOApp {

  implicit private val ec: ExecutionContext = ExecutionContext.global
  implicit private val cs: ContextShift[IO] = IO.contextShift(ec)

  private val testEndpoints = new TestEndpoints[IO]

  private val routes: HttpRoutes[IO] = Http4sServerInterpreter.toRoutes(testEndpoints.all)

  private val docs: HttpRoutes[IO]   = new SwaggerHttp4s(TestEndpoints.docs.toYaml).routes[IO]

  private val concat = routes <+> docs
  private val router = Router("/" -> concat).orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    resources.use(_ => IO.never).as(ExitCode.Success)

  private def resources: Resource[IO, Server] = for {
    blocker <- Blocker[IO]
    server  <- BlazeServerBuilder[IO](ec).bindHttp(port = 8080, host = "localhost").withHttpApp(router).resource
  } yield server

}
