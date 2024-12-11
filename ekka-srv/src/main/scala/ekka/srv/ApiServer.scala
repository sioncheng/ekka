package ekka.srv

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.sharding.typed.scaladsl.Entity
import akka.grpc.scaladsl.ServerReflection
import akka.grpc.scaladsl.ServiceHandler
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import ekka.srv.api.hi.GreeterService
import ekka.srv.api.hi.GreeterServiceHandler
import ekka.srv.api.message.MessageService
import ekka.srv.api.message.MessageServiceHandler
import ekka.srv.cluster.MessageProtocol

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Failure
import scala.util.Success


object ApiServer {
    def apply(system: ActorSystem[_],
        sharding: ClusterSharding,
        shardingRegion: ActorRef[ShardingEnvelope[MessageProtocol.Command]]) = 
            new ApiServer(system, sharding, shardingRegion)
}

class ApiServer(system: ActorSystem[_], 
    sharding: ClusterSharding, 
    shardingRegion:ActorRef[ShardingEnvelope[MessageProtocol.Command]] ) {

  def run(): Future[Http.ServerBinding] = {
    system.log.info("GreeterServer#run")

    implicit val sys = system
    implicit val ec = sys.executionContext

    val sharding = ClusterSharding(system)
    val shardingRegion: ActorRef[ShardingEnvelope[cluster.MessageProtocol.Command]] =
      sharding.init(Entity(cluster.RemoteClient.TypeKey)(ctx => cluster.RemoteClient(ctx.entityId)))

    val hello: PartialFunction[HttpRequest, Future[HttpResponse]] =
      GreeterServiceHandler.partial(GreeterServiceImpl(system))

    val messageService: PartialFunction[HttpRequest, Future[HttpResponse]] =
        MessageServiceHandler.partial(MessageServiceImpl(system, sharding, shardingRegion))

    val reflections: PartialFunction[HttpRequest, Future[HttpResponse]] =
      ServerReflection.partial(List( MessageService))

    val services: HttpRequest => Future[HttpResponse] =
      ServiceHandler.concatOrNotFound(hello, messageService, reflections)

    val bound: Future[Http.ServerBinding] =
      Http()
        .newServerAt("0.0.0.0", 8080)
        // .enableHttps(serverHttpContext)
        .bind(services)
        .map(_.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds))
    bound.onComplete {
      case Success(value) =>
        val address = value.localAddress
        system.log.info(
          s"gRPC server bound to ${address.getHostName()}:${address.getPort}"
        )
      case Failure(exception) =>
        system.log.error(
          "Failed to bind gRPC endpoint, terminating system",
          exception
        )
        system.terminate()
    }

    bound

  }
}
