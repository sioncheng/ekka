package ekka.srv

import akka.actor.typed.ActorSystem
import scala.concurrent.Future
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import ekka.srv.api.hi.GreeterServiceHandler

import scala.concurrent.duration._
import scala.util.Success
import scala.util.Failure
import akka.grpc.scaladsl.ServiceHandler
import org.checkerframework.checker.units.qual.h
import ekka.srv.api.message.MessageServiceHandler
import akka.grpc.scaladsl.ServerReflection
import ekka.srv.api.message.MessageService
import ekka.srv.api.hi.GreeterService


object ApiServer {
    def apply(system: ActorSystem[_]) = new ApiServer(system)
}

class ApiServer(system: ActorSystem[_]) {

    def run(): Future[Http.ServerBinding] = {
        system.log.info("GreeterServer#run")

        implicit val sys = system
        implicit val ec = sys.executionContext

        val hello : PartialFunction[HttpRequest, Future[HttpResponse]] = 
            GreeterServiceHandler.partial(GreeterServiceImpl(system))

        val messageService: PartialFunction[HttpRequest, Future[HttpResponse]] =
            MessageServiceHandler.partial(MessageServiceImpl(system))

        val reflections: PartialFunction[HttpRequest, Future[HttpResponse]] =
            ServerReflection.partial(List(GreeterService, MessageService))

        val services: HttpRequest => Future[HttpResponse] =
            ServiceHandler.concatOrNotFound(hello, messageService, reflections)

        val bound : Future[Http.ServerBinding] = 
            Http().newServerAt("0.0.0.0", 8080)
                //.enableHttps(serverHttpContext)
                .bind(services)
                .map(_.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds))
        bound.onComplete {
            case Success(value) => 
                val address = value.localAddress
                system.log.info(s"gRPC server bound to ${address.getHostName()}:${address.getPort}")
            case Failure(exception) => 
                system.log.error("Failed to bind gRPC endpoint, terminating system", exception)
                system.terminate()
        }

        bound
        
    }
}