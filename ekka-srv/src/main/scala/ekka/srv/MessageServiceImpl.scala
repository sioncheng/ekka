package ekka.srv

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.util.Timeout
import com.google.protobuf.ByteString
import ekka.srv.api.message.MessageReply
import ekka.srv.api.message.MessageRequest
import ekka.srv.api.message.MessageService
import ekka.srv.cluster.MessageProtocol

import scala.concurrent.Future
import scala.concurrent.duration._

object MessageServiceImpl{
    def apply(system: ActorSystem[_],
      sharding: ClusterSharding,
      shardingRegion: ActorRef[ShardingEnvelope[MessageProtocol.Command]]) = 
        new MessageServiceImpl(system, sharding, shardingRegion)
}

class MessageServiceImpl(system: ActorSystem[_],
  sharding: ClusterSharding,
  shardingRegion: ActorRef[ShardingEnvelope[MessageProtocol.Command]]) extends MessageService {
  override def sendMessage(in: MessageRequest): Future[MessageReply] = {
    system.log.info("sendMessage {} {} {}", in.id, in.messageType, in.messagePayload)
    //val messageReq = MessageProtocol.MessageReq(in)
    val client = sharding.entityRefFor(MessageProtocol.RemoteClientTypeKey, in.remote)
    //TODO ask
    implicit val timeout : Timeout = 2.seconds
    implicit val ec = system.executionContext
    val future = client.ask(ref => MessageProtocol.MessageReq(in, ref))
    //Future.successful(MessageReply(in.id, in.remote, in.messageType * -1 , ByteString.fromHex("ab")))
    future.map(messageRes2MessageReply)
  }

  def messageRes2MessageReply(mr: MessageProtocol.MessageRes): MessageReply = {
    val msg = mr.message
    MessageReply(msg.id, msg.remote, msg.messageType, msg.messagePayload, msg.resType)
  }
}
