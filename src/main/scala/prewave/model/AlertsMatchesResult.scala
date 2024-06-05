package prewave.model

import sangria.macros.derive.{GraphQLDescription, GraphQLField, deriveObjectType}
import sangria.schema.ObjectType

import scala.concurrent.Future

case class AlertsMatchesResult(@GraphQLDescription("Query terms with matched alerts")
                               matches: Seq[Term2AlertsMatches],
                               @GraphQLDescription("All query terms from request (doesn't depend on matches)")
                               allTerms: Seq[QueryTerm],
                               @GraphQLDescription("All alerts from request (doesn't depend on matches)")
                               allAlerts: Seq[Alert]) {
  @GraphQLField @GraphQLDescription("Number of terms which were matched")
  def countTerms(): Future[Int] = Future.successful(matches.length)
  @GraphQLField @GraphQLDescription("Summary number of all alerts of all matches")
  def countAllMatches(): Future[Int] = Future.successful(matches.map(_.alerts.length).sum)
}

object AlertsMatchesResult {
  implicit val AlertsMatchesResultType: ObjectType[Unit, AlertsMatchesResult] = deriveObjectType[Unit, AlertsMatchesResult]()
}
