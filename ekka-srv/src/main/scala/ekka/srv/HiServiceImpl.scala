package ekka.srv

import ekka.srv.api.hi.HiService
import akka.actor.typed.ActorSystem
import ekka.srv.api.hi.{HiReply, HiRequest}
import scala.concurrent.Future

object HiServiceImpl {
    def apply(system: ActorSystem[_]) = new HiServiceImpl(system)
}

class HiServiceImpl(system: ActorSystem[_]) extends HiService {
  override def sayHi(in: HiRequest): Future[HiReply] = {
    system.log.info(s"hi request {}", in.name)
    Future.successful(HiReply("Hi " + in.name))
  }
}
