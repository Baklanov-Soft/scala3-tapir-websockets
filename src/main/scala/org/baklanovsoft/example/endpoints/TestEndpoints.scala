package org.baklanovsoft.example.endpoints

import cats.implicits._
import sttp.tapir._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.effect.IO
import org.http4s.HttpRoutes
import cats.effect.{ContextShift, Timer}
import cats.effect.Sync
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.docs.openapi._
import sttp.tapir.server.ServerEndpoint

class TestEndpoints[F[_]: Sync] {
  import TestEndpoints._

  private val countLogic: ServerEndpoint[String, Unit, Int, Any, F] =
    countEndpoint.serverLogic { str =>
      Sync[F].delay {
        val count = str.length
        count.asRight[Unit]
      }
    }

  val all = List(countLogic)

}

object TestEndpoints {

  private val API_ROOT = "api" / "v1"

  val countEndpoint: Endpoint[String, Unit, Int, Any] =
    endpoint.post
      .in(API_ROOT)
      .in("count")
      .in(stringBody)
      .out(plainBody[Int])

  val docs: OpenAPI =
    OpenAPIDocsInterpreter.toOpenAPI(
      List(
        countEndpoint
      ),
      "Test",
      "1.0"
    )

}
