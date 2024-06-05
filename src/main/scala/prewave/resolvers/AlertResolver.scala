package prewave.resolvers

import prewave.matcher.MatcherViaRegex
import prewave.model.AlertsMatchesResult
import prewave.sttp.HttpRequestSender

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object AlertResolver {
  def matchAlerts(): Future[AlertsMatchesResult] = {
    for {
      terms <- HttpRequestSender.getQueryTerms
      alerts <- HttpRequestSender.getAlerts
      result <- Future.successful(MatcherViaRegex.`match`(terms, alerts))
    } yield result
  }
}
