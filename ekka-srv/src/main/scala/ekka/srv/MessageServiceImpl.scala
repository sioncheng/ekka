package ekka.srv

import akka.actor.typed.ActorSystem
import ekka.srv.api.message.MessageService
import ekka.srv.api.message.{MessageReply, MessageRequest}
import scala.concurrent.Future
import com.google.protobuf.ByteString
import scala.annotation.switch
import akka.actor.typed.ActorRef
import akka.cluster.sharding.typed.ShardingEnvelope

object MessageServiceImpl {
  def apply(system: ActorSystem[_], 
    shardingRegion: ActorRef[ShardingEnvelope[cluster.MessageProtocol.Command]]) = 
      new MessageServiceImpl(system, shardingRegion)
}

class MessageServiceImpl(system: ActorSystem[_],
  shardingRegion: ActorRef[ShardingEnvelope[cluster.MessageProtocol.Command]]) extends MessageService {
  override def sendMessage(in: MessageRequest): Future[MessageReply] = {
    system.log.info(
      "sendMessage {} {} {}",
      in.id, in.messageType, in.messagePayload
    )

    shardingRegion ! ShardingEnvelope(in.remote, cluster.MessageProtocol.MessageReq(in))

    Future.successful(
      MessageReply(
        in.id,
        in.remote,
        in.messageType * -1,
        ByteString.fromHex("ab")
      )
    )
  }
}
