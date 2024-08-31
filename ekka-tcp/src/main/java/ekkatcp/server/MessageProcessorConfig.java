package ekkatcp.server;

public class MessageProcessorConfig {

    private String srvGrpcHost;

    private int srvGrpcPort;

    public String getSrvGrpcHost() {
        return srvGrpcHost;
    }

    public void setSrvGrpcHost(String srvGrpcHost) {
        this.srvGrpcHost = srvGrpcHost;
    }

    public int getSrvGrpcPort() {
        return srvGrpcPort;
    }

    public void setSrvGrpcPort(int srvGrpcPort) {
        this.srvGrpcPort = srvGrpcPort;
    }

    public MessageProcessorConfig() {
        this.srvGrpcHost = "";
        this.srvGrpcPort = 0;
    }

    public MessageProcessorConfig(String host, int port) {
        this.srvGrpcHost = host;
        this.srvGrpcPort = port;
    }
}
