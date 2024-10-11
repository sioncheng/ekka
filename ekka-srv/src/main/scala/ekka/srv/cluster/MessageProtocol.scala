package ekka.srv.cluster

import ekka.srv.api.message.MessageRequest
import ekka.srv.api.message.MessageReply
import akka.actor.typed.ActorRef

object  MessageProtocol {
  sealed trait Command
  case class MessageReq(message: MessageRequest, replyTo: ActorRef[Command]) extends Command
  case class MessageRes(message: MessageReply) extends Command
}
