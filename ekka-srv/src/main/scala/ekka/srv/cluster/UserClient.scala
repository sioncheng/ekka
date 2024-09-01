package ekka.srv.cluster

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import ekka.srv.cluster.MessageProtocol.MessageReq

class UserClient {
  
}

object UserClient {
  def apply(entityId:String): Behavior[MessageProtocol.Command] = 
    Behaviors.setup {ctx =>
      Behaviors.receiveMessage[MessageProtocol.Command] { msg =>
        msg match {
            case MessageReq(message) => 
                ctx.log.info("req {}", message.id, message.remote)
                Behaviors.same
            case x: Any =>
                ctx.log.warn("unknown message {}", x)
                Behaviors.same
        }
      }
    }
  
}
