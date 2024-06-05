package prewave

import config.Config.config.graphqlServer.{host, port}
import graphql.GraphQLServer.graphqlEndpoint
import org.apache.pekko.actor.{ActorSystem, CoordinatedShutdown}
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.server.Route

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Main extends App {
  implicit private val actorSystem: ActorSystem = ActorSystem("graphql-server")
  implicit private val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  private val route: Route = path("graphql") {
    post {
      entity(as[String]) { requestJson =>
        graphqlEndpoint(requestJson)
      }
    } ~
      get {
        getFromResource("graphiql.html")
      }
  }

  private val serverBinding = Http().newServerAt(host, port).bind(route)

  serverBinding.onComplete {
    case Success(binding) =>
      println(s"Server online at http://localhost:${port}/graphql")
      CoordinatedShutdown(actorSystem).addJvmShutdownHook {
        println("Shutting down the server...")
        binding.unbind().onComplete(_ => actorSystem.terminate())
      }
    case Failure(exception) =>
      println(s"Failed to bind HTTP server: ${exception.getMessage}")
      actorSystem.terminate()
  }

  Await.result(actorSystem.whenTerminated, Duration.Inf)
}