package prewave.graphql

import prewave.model.AlertsMatchesResult
import prewave.resolvers.AlertResolver
import sangria.schema._

object GraphQLSchema {
  private val QueryType: ObjectType[Unit, Unit] = ObjectType(
    "Query",
    fields[Unit, Unit](
      Field("getAlertMatches", AlertsMatchesResult.AlertsMatchesResultType,
        resolve = _ => AlertResolver.matchAlerts())
    )
  )

  val schema: Schema[Unit, Unit] = Schema(QueryType)
}


