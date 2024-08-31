package ekkatcp.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<TcpMessage> {

   @Override
   protected void encode(ChannelHandlerContext ctx, TcpMessage msg, ByteBuf out) throws Exception {
        int payloadLength = msg.getMessagePayload() == null ? 0 : msg.getMessagePayload().length;
        int messageLenth = 2 + 1 + payloadLength;
        out.writeInt(messageLenth);
        out.writeByte(69);
        out.writeByte(65);
        out.writeByte(msg.getMessageType());
        if (null != msg.getMessagePayload()) {
            out.writeBytes(msg.getMessagePayload());
        }
   }
}
