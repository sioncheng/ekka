package ekka.srv.cluster

import ekka.srv.api.message.MessageRequest
import ekka.srv.api.message.MessageReply
import akka.actor.typed.ActorRef

import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.actor.typed.ActorRef


object  MessageProtocol {
  sealed trait Command
  case class MessageReq(message: MessageRequest, replyTo: ActorRef[MessageRes]) extends Command
  case class MessageRes(message: MessageReply) extends Command

  val RemoteClientTypeKey = EntityTypeKey[Command]("RemoteClient")
}
