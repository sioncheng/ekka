package ekka.srv.cluster

import ekka.srv.api.message.MessageRequest
import ekka.srv.api.message.MessageReply

object  MessageProtocol {
  sealed trait Command
  case class MessageReq(message: MessageRequest) extends Command
  case class MessageRes(message: MessageReply) extends Command
}
