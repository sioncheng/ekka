package ekkatcp.server;


import org.checkerframework.checker.units.qual.t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ekka.srv.api.message.MessageReply;
import ekka.srv.api.message.MessageRequest;
import ekka.srv.api.message.MessageServiceGrpc;
import ekka.srv.api.message.MessageServiceGrpc.MessageServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

public class Server {

    private final ServerConfig config;
    private final MessageProcessorConfig processorConfig;
    private final EventLoopGroup eventLoopGroup;
    private volatile int state;

    private static final Logger log = LoggerFactory.getLogger(Server.class); 


    public Server(ServerConfig serverConfig, MessageProcessorConfig processorConfig) {
        this.config = serverConfig;
        this.processorConfig = processorConfig;
        this.eventLoopGroup = new NioEventLoopGroup();
        this.state = 0;
    }

    public void start()  {

        try {
            final int workerThreads = this.config.getWorkerThreads();
            final MessageProcessor messageProcessor = new ServerMessageProcessor(this.processorConfig);

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MessageDecoder())
                            .addLast(new MessageEncoder())
                            .addLast(new UnorderedThreadPoolEventExecutor(workerThreads), 
                                new TcpMessageHandler(messageProcessor));
                    }
                })
                .bind(this.config.getHost(), this.config.getPort())
                .sync();
            this.state = 1;

            log.info("server started {} {}", this.config.getHost(), this.config.getPort());
        
        } catch (Exception ex) {
            log.error("server exception", ex);
            stop();
        }



        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
        
            bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MessageDecoder())
                            .addLast(new MessageEncoder())
                            .addLast(new TcpMessageHandler(null));
                    }
                })
                .connect(this.config.getHost(), this.config.getPort())
                .sync();
        } catch (Exception ex) {
            log.error("bootstrap exception", ex);
            
        }
    }

    public void stop() {
        if (this.state == 2) {
            return;
        }
        this.state = 2;
        eventLoopGroup.shutdownGracefully();
    }
}
