package ekka.srv.cluster

import ekka.srv.api.message.MessageRequest
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import ekka.srv.cluster.MessageProtocol.MessageReq
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.cluster.sharding.typed.scaladsl.EntityRef
import akka.actor.typed.ActorRef
import akka.cluster.sharding.typed.ShardingEnvelope
import ekka.srv.MessageType

class RemoteClient {}

object RemoteClient {

  val TypeKey = EntityTypeKey[MessageProtocol.Command]("RemoteClient")

  def apply(entityId: String): Behavior[MessageProtocol.Command] =
    Behaviors.setup { ctx =>
      ctx.log.info("setup remote client")
      receive(entityId)
    }

  def receive(
      entityId: String
  ): Behavior[MessageProtocol.Command] = Behaviors.receive { (ctx, msg) =>
    msg match {
      case MessageReq(message, replyTo) =>
        message.messageType match {
          case MessageType.PING =>
            ctx.log.info("piing")
          case MessageType.SIGNIN_REQ =>
            ctx.log.info("signin")
          case MessageType.SEND_MSG =>
            ctx.log.info("send message")
          case x: Any =>
            ctx.log.info("what? {}", x)
        }
        Behaviors.same
      case x: Any =>
        ctx.log.warn("unexpected message req {}", x)
        Behaviors.same
    }
  }
}
