package ekkatcp.server;

import io.netty.channel.ChannelHandlerContext;

public interface MessageProcessor {
    void active(ChannelHandlerContext ctx);
    void inactive(ChannelHandlerContext ctx);
    void processMessage(TcpMessage tcpMessage, ChannelHandlerContext ctx);
}
