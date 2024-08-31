package ekka.srv

import ekka.srv.api.helloworld.GreeterService
import ekka.srv.api.helloworld.HelloRequest
import scala.concurrent.Future
import ekka.srv.api.helloworld.HelloReply
import akka.actor.typed.ActorSystem

object GreeterServiceImpl {
  def apply(system: ActorSystem[_]) = new GreeterServiceImpl(system)
}

class GreeterServiceImpl(system: ActorSystem[_]) extends GreeterService {
  override def sayHello(req: HelloRequest): Future[HelloReply] = {
    system.log.info(s"hello request {}", req.name)
    Future.successful(HelloReply("hello " + req.name))
  }
}
