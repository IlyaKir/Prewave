package prewave

import config.Config.config.graphqlServer.{host, port}
import graphql.GraphQLServer.graphqlEndpoint
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.server.Route

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

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

  println(s"Server is online. Please navigate to http://${host}:${port}/graphql\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return

  serverBinding
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => actorSystem.terminate()) // and shutdown when done
}