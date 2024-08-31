package ekkatcp.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class MessageDecoder extends LengthFieldBasedFrameDecoder{

    public MessageDecoder() {
        super(16 * 1024, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    
        ByteBuf msg = (ByteBuf) super.decode(ctx, in);
        if (msg == null) {
            return null;
        }

        try {
            int i1 = msg.readByte();
            int i2 = msg.readByte();

            if (i1 != 69 || i2 != 65) {
                throw new RuntimeException("wrong message " + i1 + "," + i2);
            }
            
            TcpMessage tcpMessage = new TcpMessage();
            tcpMessage.setMessageType(msg.readByte());
            if (msg.readableBytes() > 0) {
                byte[] data = new byte[msg.readableBytes()];
                msg.readBytes(data);
                tcpMessage.setMessagePayload(data);
            } else {
                tcpMessage.setMessagePayload(null);
            }

            return tcpMessage;
        } finally {
            msg.release();
        }   
    }
}
