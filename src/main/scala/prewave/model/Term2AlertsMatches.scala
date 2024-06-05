package prewave.model

import sangria.macros.derive.{GraphQLDescription, GraphQLField, deriveObjectType}
import sangria.schema.ObjectType

import scala.concurrent.Future

case class Term2AlertsMatches(term: QueryTerm, alerts: Seq[Alert]) {
  @GraphQLField @GraphQLDescription("Number of alerts for current query term")
  def countAlerts(): Future[Int] = Future.successful(alerts.length)
}
object Term2AlertsMatches {
  implicit val Term2AlertsType: ObjectType[Unit, Term2AlertsMatches] = deriveObjectType[Unit, Term2AlertsMatches]()
}
