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
    public void processMessage(TcpMessage tcpMessage, ChannelHandlerContext ctx) {
        byte t = tcpMessage.getMessageType();
        log.info("processMessage {}", t);

        switch (t) {
            case 0:
                process0(tcpMessage, ctx);
                break;
            default:
                break;
        }
    }

    private void process0(TcpMessage tcpMessage, ChannelHandlerContext ctx) {
        MessageRequest request = MessageRequest.newBuilder()
            .setId(ctx.channel().remoteAddress().toString())
            .setMessageType(tcpMessage.getMessageType())
            .setMessagePayload(tcpMessage.getMessagePayload() == null ? ByteString.EMPTY: ByteString.copyFrom(tcpMessage.getMessagePayload()))
            .build();

        messageServiceStub.sendMessage(request, new StreamObserver<MessageReply>() {
            @Override
            public void onNext(MessageReply value) {
                log.info("onNext {} {}", value, ctx.channel().remoteAddress());
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

    // private void reply(MessageReply reply) {
    //     String id = reply.getId();
    //     ChannelHandlerContext ctx = remoteMap.get(id);
    //     log.info("reply {} {}", id, ctx.channel().remoteAddress());
    // }
}
