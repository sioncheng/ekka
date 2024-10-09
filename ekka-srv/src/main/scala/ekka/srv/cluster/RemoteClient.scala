package ekka.srv.cluster

import ekka.srv.api.message.MessageRequest
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import ekka.srv.cluster.MessageProtocol.MessageReq
import akka.actor.ActorRef

class RemoteClient {}

object RemoteClient {

  def apply(entityId: String): Behavior[MessageProtocol.Command] =
    Behaviors.setup { ctx =>
      ctx.log.info("setup remote client")
      receive(entityId, Map.empty)
    }

  def receive(
      entityId: String,
      map: Map[String, ActorRef]
  ): Behavior[MessageProtocol.Command] = Behaviors.receive { (ctx, msg) =>
    msg match {
      case MessageReq(message) =>
        message.messageType match {
          case 0 =>
            if (map.contains(message.remote)) {
              // TODO reply
            } else {
              ctx.log.warn("no memory for remote {}", message.remote)
            }
        }
        Behaviors.same
      case x: Any =>
        ctx.log.warn("unexpected message req {}", x)
        Behaviors.same
    }
  }
}
