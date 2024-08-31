package ekkatcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TcpMessageHandler extends SimpleChannelInboundHandler<TcpMessage> {

    private final MessageProcessor messageProcessor;

    private static final Logger log = LoggerFactory.getLogger(TcpMessageHandler.class);

    public TcpMessageHandler(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (messageProcessor == null) {
            TcpMessage tcpMessage = new TcpMessage();
            tcpMessage.setMessageType((byte)0);

            ctx.writeAndFlush(tcpMessage);
        } else {
            messageProcessor.active(ctx);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (messageProcessor != null) {
            messageProcessor.inactive(ctx);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TcpMessage msg) throws Exception {
        if (msg == null) {
            return;
        }

        log.info("channelRead0 {}", msg.getMessageType());
        if (messageProcessor != null) {
            messageProcessor.processMessage(msg, ctx);
        }
    }
}
