package prewave.graphql

import io.circe.Json
import io.circe.jawn.decode
import io.circe.syntax._
import org.apache.pekko.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.server.Route
import sangria.ast.Document
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.circe._
import sangria.parser.QueryParser

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object GraphQLServer {
  def graphqlEndpoint(requestJson: String)(implicit executionContext: ExecutionContext): Route = {
    val graphQLBody = decode[GraphQLBody](requestJson).getOrElse(GraphQLBody())

    QueryParser.parse(graphQLBody.query) match {
      case Success(queryAst: Document) =>
        complete {
          Executor.execute(
            schema = GraphQLSchema.schema,
            queryAst = queryAst,
            operationName = graphQLBody.operationName,
            variables = graphQLBody.variables.getOrElse(Json.obj())
          ).map { result =>
            HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, result.asJson.noSpaces))
          }.recover {
            case error: QueryAnalysisError => HttpResponse(400, entity = HttpEntity(ContentTypes.`application/json`, error.resolveError.asJson.noSpaces))
            case error: ErrorWithResolver => HttpResponse(500, entity = HttpEntity(ContentTypes.`application/json`, error.resolveError.asJson.noSpaces))
          }
        }
      case Failure(error) =>
        complete(HttpResponse(400, entity = HttpEntity(error.getMessage)))
    }
  }
}
