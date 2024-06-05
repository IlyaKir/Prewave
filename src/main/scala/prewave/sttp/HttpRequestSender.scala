package prewave.sttp

import io.circe.Decoder
import io.circe.parser.decode
import prewave.config.Config
import prewave.model.{Alert, QueryTerm}
import prewave.model.Alert.alertDecoder
import prewave.model.QueryTerm.queryTermDecoder
import prewave.logger.Logger
import sttp.client4.httpclient.HttpClientFutureBackend
import sttp.client4.{Request, Response, UriContext, basicRequest}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HttpRequestSender {
  private val backend = HttpClientFutureBackend()

  private lazy val queryTermsRequest: Request[Either[String, String]] = basicRequest
    .get(uri"${Config.config.prewaveServer.testQueryTermAddress}")
    .header("Accept", "application/json")

  private lazy val alertsRequest: Request[Either[String, String]] = basicRequest
    .get(uri"${Config.config.prewaveServer.testAlertsAddress}")
    .header("Accept", "application/json")

  private def handleResponse(response: Response[Either[String, String]]) =
    response.body match {
      case Left(error) =>
        Logger.logger.error(s"\tCode Response: <${response.code}>\nError Message: $error")
        Future.failed(new Exception(error))
      case Right(value) =>
        Logger.logger.info(s"\tSuccessfully received response. Length ${value.length}")
        Future.successful(value)
    }

  private def parseJson[T: Decoder](jsonString: String) = {
    decode[Seq[T]](jsonString) match {
      case Left(error) =>
        Logger.logger.error(s"\tFailed to parse response; ${error}")
        Future.failed(error)
      case Right(value) =>
        Logger.logger.info(s"\tSuccessfully parsed response into")
        Future.successful(value)
    }
  }

  private def get[T: Decoder](request: Request[Either[String, String]]): Future[Seq[T]] =
    for {
      response <- backend.send(request)
      jsonString <- handleResponse(response)
      result <- parseJson[T](jsonString)
    } yield result

  def getQueryTerms: Future[Seq[QueryTerm]] = {
    Logger.logger.info(s"Getting QueryTerms from Prewave server")
    get[QueryTerm](queryTermsRequest)
  }

  def getAlerts: Future[Seq[Alert]] = {
    Logger.logger.info(s"Getting Alerts from Prewave server")
    get[Alert](alertsRequest)
  }
}
