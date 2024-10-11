package ekkatcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

import ekka.srv.api.message.MessageReply;
import ekka.srv.api.message.MessageRequest;
import ekka.srv.api.message.MessageServiceGrpc;
import ekka.srv.api.message.MessageServiceGrpc.MessageServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerMessageProcessor implements MessageProcessor {

    private static Logger log = LoggerFactory.getLogger(ServerMessageProcessor.class);

    private final MessageProcessorConfig config;

    private final MessageServiceStub messageServiceStub;

    private final AtomicInteger counter = new AtomicInteger();

    private final ConcurrentHashMap<String, ChannelHandlerContext> remoteMap =
        new ConcurrentHashMap<>();

    public ServerMessageProcessor(MessageProcessorConfig config) {
        this.config = config;
        ManagedChannel channel = ManagedChannelBuilder.forAddress(this.config.getSrvGrpcHost(), 
                this.config.getSrvGrpcPort())
            .usePlaintext()
            .build();
        messageServiceStub = MessageServiceGrpc.newStub(channel);
    }

    @Override
    public void active(ChannelHandlerContext ctx) {
        remoteMap.put(ctx.channel().remoteAddress().toString(), ctx);
        int n = counter.incrementAndGet();
        log.info("active {} {}", n, ctx.channel().remoteAddress().toString());
    }

    @Override
    public void inactive(ChannelHandlerContext ctx) {
        remoteMap.remove(ctx.channel().remoteAddress().toString());
        int n = counter.decrementAndGet();
        log.info("inactive {} {}", n, ctx.channel().remoteAddress().toString());
    }

    @Override
    public void processTcpMessage(TcpMessage tcpMessage, ChannelHandlerContext ctx) {
        byte t = tcpMessage.getMessageType();
        log.info("processMessage {}", t);

        switch (t) {
            case MessageType.SIGNIN_REQ:
                processSignin(tcpMessage, ctx);
                break;
            case MessageType.PING:
                processPing(tcpMessage, ctx);
                break;
            case MessageType.SEND_MSG:
                processSendMessage(tcpMessage, ctx);
                break;
            default:
                break;
        }
    }

    private void processSignin(TcpMessage tcpMessage, ChannelHandlerContext ctx) {
        forwardToServer(tcpMessage, ctx);
    }

    private void processPing(TcpMessage tcpMessage, ChannelHandlerContext ctx) {
        writeTcpMessage(ctx, MessageType.PONG, null);
        forwardToServer(tcpMessage, ctx);
    }

    private void processSendMessage(TcpMessage tcpMessage, ChannelHandlerContext ctx) {
        forwardToServer(tcpMessage, ctx);
    }

    private void forwardToServer(TcpMessage tcpMessage, ChannelHandlerContext ctx) {
        ByteString messagePayload = 
        tcpMessage.getMessagePayload() == null ? ByteString.EMPTY: ByteString.copyFrom(tcpMessage.getMessagePayload());
        MessageRequest request = MessageRequest.newBuilder()
            .setId(ctx.channel().remoteAddress().toString())
            .setRemote(ctx.channel().remoteAddress().toString())
            .setMessageType(tcpMessage.getMessageType())
            .setMessagePayload(messagePayload)
            .build();

        messageServiceStub.sendMessage(request, new StreamObserver<MessageReply>() {
            @Override
            public void onNext(MessageReply value) {
                log.info("onNext {} {}", value, ctx.channel().remoteAddress());
                replyToClient(value);
            }

            @Override
            public void onError(Throwable t) {
                log.error("onError", t);
            }

            @Override
            public void onCompleted() {
                log.info("onComplete process0 {}", request);
            }
        });
    }

    private void replyToClient(MessageReply reply) {
        String remote = reply.getRemote();
        ChannelHandlerContext ctx = remoteMap.get(remote);
        if (null == ctx) {
            log.warn("remote {} no ctx", remote);
            return;
        }

        log.info("reply {} {}", remote, ctx.channel().remoteAddress());
        writeTcpMessage(ctx, (byte)reply.getMessageType(), reply.getMessagePayload().toByteArray());
    }

    private void writeTcpMessage(ChannelHandlerContext ctx, byte type, byte[] payload) {
        TcpMessage tcpMessage = new TcpMessage();
        tcpMessage.setMessageType(type);
        tcpMessage.setMessagePayload(payload);
    
        ctx.writeAndFlush(tcpMessage).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("write to {}", ctx.channel().remoteAddress());
            }
        });
    }
}
