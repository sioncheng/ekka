package ekkatcp.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;

import java.util.Arrays;

public class MessageEncoderTest {

    @Test
    public void testEncoderWithNullPayload() {
        EmbeddedChannel channel = new EmbeddedChannel(new MessageEncoder());
        TcpMessage tcpMessage = new TcpMessage();
        tcpMessage.setMessageType((byte)0);
        tcpMessage.setMessagePayload(null);
        channel.writeOutbound(tcpMessage);

        channel.finish();

        ByteBuf buf = (ByteBuf) channel.readOutbound();
        Assertions.assertNotNull(buf);
        Assertions.assertEquals(7, buf.readableBytes());
        int messageLenth = buf.readInt();
        Assertions.assertEquals(3, messageLenth);
        Assertions.assertEquals((byte)'E', buf.readByte());
        Assertions.assertEquals((byte)'A', buf.readByte());
        Assertions.assertEquals(tcpMessage.getMessageType(), buf.readByte());
    }

    @Test
    public void testEncoderWithPayload() {
        EmbeddedChannel channel = new EmbeddedChannel(new MessageEncoder());
        TcpMessage tcpMessage = new TcpMessage();
        tcpMessage.setMessageType((byte)0);
        tcpMessage.setMessagePayload("payload".getBytes());
        channel.writeOutbound(tcpMessage);

        channel.finish();

        ByteBuf buf = (ByteBuf) channel.readOutbound();
        Assertions.assertNotNull(buf);
        Assertions.assertEquals(14, buf.readableBytes());
        int messageLenth = buf.readInt();
        Assertions.assertEquals(10, messageLenth);
        Assertions.assertEquals((byte)'E', buf.readByte());
        Assertions.assertEquals((byte)'A', buf.readByte());
        Assertions.assertEquals(tcpMessage.getMessageType(), buf.readByte());
        byte[] rest = new byte[buf.readableBytes()];
        buf.readBytes(rest);
    
        Assertions.assertTrue(Arrays.equals(tcpMessage.getMessagePayload(), rest));
    }
}
