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
import akka.actor.ActorRef
import org.checkerframework.checker.units.qual.m
import ekka.srv.cluster.MessageProtocol.MessageRes
import ekka.srv.api.message.MessageReply
import akka.actor.typed.scaladsl.ActorContext

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

    // def receive(entityId: String, map: Map[String, String]): 
    //     Behavior[MessageProtocol.Command] = Behaviors.receive {(ctx, msg) =>
    //         msg match {
    //             case MessageReq(message, replyTo) =>
    //                 message.reqType match {
    //                     case 1 =>
    //                         receiveForward(entityId, map, msg, ctx)
    //                     case x =>
    //                         ctx.log.warn("unexpected message req {}", x)
    //                         Behaviors.same
    //                 }
    //             case x: Any =>
    //                 ctx.log.warn("unexpected message in receive req {}", x)
    //                 Behaviors.same
    //         }
    //     }
    
    // def receiveForward(entityId: String, 
    //     map: Map[String, String], 
    //     msg: MessageProtocol.Command, 
    //     ctx: ActorContext[MessageProtocol.Command]): Behavior[MessageProtocol.Command] = {
    //         msg match {
    //             case MessageReq(message, replyTo) => 
    //                 message.messageType match {
    //                     case 0 =>
    //                         //heartbeat
    //                         if (map.contains(message.remote)) {
    //                         ctx.log.info("remote {} hearbeat", message.remote)
    //                             val mr = MessageReply(message.id,
    //                                 message.remote, 
    //                                 message.messageType * -1, 
    //                                 message.messagePayload)
    //                             replyTo ! MessageRes(mr)
    //                         } else {
    //                             ctx.log.warn("no memory for remote {}", message.remote)
    //                         }
    //                         Behaviors.same
    //                     case 1 =>
    //                         //login
    //                         //todo authenticate
    //                         ctx.log.info("remote {} login", message.remote)
    //                         val mr = MessageReply(message.id,
    //                                 message.remote, 
    //                                 message.messageType * -1, 
    //                                 message.messagePayload)
    //                             replyTo ! MessageRes(mr)
    //                         receive(entityId, map + (message.remote -> "1"))
    //                 }
    //             case x: Any =>
    //                 ctx.log.warn("unexpected message in forward req {}", x)
    //                 Behaviors.same
    //         }
    // }
}
