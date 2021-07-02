package org.baklanovsoft.example.endpoints

import cats.implicits._
import sttp.tapir._
import sttp.tapir.docs.asyncapi.AsyncAPIInterpreter
import EndpointOps._
import sttp.capabilities.fs2.Fs2Streams
import fs2.Pipe
import cats.effect.Sync
import fs2.Stream

class WSEndpoints[F[_]: Sync] {

  import WSEndpoints._

  // you can also place it inside "Right"
  private def helloPipe: Pipe[F, String, String] =
    stream => stream.map(origString => s"Hello, $origString")

  // The server logic must return value encapsulating the logic of the web socket. It will be a Pipe.
  private val wsLogic = webSocketEndpoint[F].serverLogic { (str: String) =>
    Sync[F].delay(
      Right(
        helloPipe
      )
    )
  }

  val all = List(wsLogic)
}

object WSEndpoints {


  // just to see the type of body
  // WebSocketBodyOutput[PIPE_REQ_RESP, REQ, RESP, T, S]
  /* we’ll in fact need two types and two codecs: one for messages that are sent to the server, and one for messages that are received */

  /** In summary, a WebSocketBodyOutput contains:
    *
    *    - a Codec[WebSocketFrame, REQ, REQ_CF] and Codec[WebSocketFrame, RESP, RESP_CF] for mapping request/responses frames into higher-level types
    *    - a Codec[Pipe[REQ, RESP], T, CF] for further mapping the complete pipe into another type
    *    - endpoint meta-data (e.g. description)
    *    - concatenateFragmentedFrames flag: whether the codecs can handle fragmented frames, or should they be concatenated beforehand
    *    - ignorePong, autoPongOnPing, decodeCloseRequests, decodeCloseResponses, autoPing configure control frame handling
    */

  private def out[F[_]]
      : WebSocketBodyOutput[Pipe[F, String, String], String, String, Pipe[F, String, String], Fs2Streams[F]] =
    webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.TextPlain](Fs2Streams[F])

  def webSocketEndpoint[F[_]] =
    endpoint
      .in(ASYNC_ROOT)
      .in(plainBody[String])
      .out(webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.TextPlain](Fs2Streams[F]))
      .summary("Test websocket endpoint for AsyncAPI docs")

  def docs[F[_]] = AsyncAPIInterpreter().toAsyncAPI(
    List(webSocketEndpoint[F]),
    title = "test websocket",
    version = "1.0"
  )
}
