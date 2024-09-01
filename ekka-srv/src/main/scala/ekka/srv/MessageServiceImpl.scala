package ekka.srv

import akka.actor.typed.ActorSystem
import ekka.srv.api.message.MessageService
import ekka.srv.api.message.{MessageReply, MessageRequest}
import scala.concurrent.Future
import com.google.protobuf.ByteString

object MessageServiceImpl{
    def apply(system: ActorSystem[_]) = new MessageServiceImpl(system)
}

class MessageServiceImpl(system: ActorSystem[_]) extends MessageService {
  override def sendMessage(in: MessageRequest): Future[MessageReply] = {
    system.log.info("sendMessage {} {} {}", in.id, in.messageType, in.messagePayload)
    Future.successful(MessageReply(in.id, in.remote, in.messageType * -1 , ByteString.fromHex("ab")))
  }
}
