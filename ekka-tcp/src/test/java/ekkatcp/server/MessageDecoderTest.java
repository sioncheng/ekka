package ekkatcp.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

public class MessageDecoderTest {

    @Test
    public void testDecoderWithNullPayload() {

        TcpMessage msg = new TcpMessage();
        msg.setMessageType((byte)0);
        msg.setMessagePayload(null);

        int payloadLength = msg.getMessagePayload() == null ? 0 : msg.getMessagePayload().length;
        int messageLenth = 2 + 1 + payloadLength;

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(messageLenth);
        buf.writeByte((byte)'E');
        buf.writeByte((byte)'A');
        buf.writeByte(msg.getMessageType());

        EmbeddedChannel channel = new EmbeddedChannel(new MessageDecoder());
        channel.writeInbound(buf);
        channel.finish();
        TcpMessage tcpMessage = (TcpMessage)channel.readInbound();

        Assertions.assertEquals(0, tcpMessage.getMessageType());
        
    }

    @Test
    public void testDecoderWithPayload() {
        TcpMessage msg = new TcpMessage();
        msg.setMessageType((byte)0);
        msg.setMessagePayload("payload".getBytes());

        int payloadLength = msg.getMessagePayload() == null ? 0 : msg.getMessagePayload().length;
        int messageLenth = 2 + 1 + payloadLength;

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(messageLenth);
        buf.writeByte((byte)'E');
        buf.writeByte((byte)'A');
        buf.writeByte(msg.getMessageType());
        buf.writeBytes(msg.getMessagePayload());

        EmbeddedChannel channel = new EmbeddedChannel(new MessageDecoder());
        channel.writeInbound(buf);
        channel.finish();

        TcpMessage tcpMessage = (TcpMessage)channel.readInbound();

        Assertions.assertEquals(0, tcpMessage.getMessageType());
        Assertions.assertEquals("payload", new String(tcpMessage.getMessagePayload()));
    }

    @Test
    public void testDecoderWithWrongPayload() {

        TcpMessage msg = new TcpMessage();
        msg.setMessageType((byte)0);
        msg.setMessagePayload("payload".getBytes());

        int payloadLength = msg.getMessagePayload() == null ? 0 : msg.getMessagePayload().length;
        int messageLenth = 2 + 1 + payloadLength;

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(messageLenth);
        buf.writeByte((byte)'E');
        buf.writeByte((byte)'A');
        buf.writeByte(msg.getMessageType());
        //buf.writeBytes(msg.getMessagePayload());

        EmbeddedChannel channel = new EmbeddedChannel(new MessageDecoder());
        channel.writeInbound(buf);
        channel.finish();
        
        TcpMessage tcpMessage = (TcpMessage)channel.readInbound();
        Assertions.assertNull(tcpMessage);
        
    }
}
