package ekkatcp.server;

public class ServerConfig {

    private String host;

    private int port;

    private int workerThreads;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public ServerConfig(String host, int port, int workerThreads) {
        this.host = host;
        this.port = port;
        this.workerThreads = workerThreads;
    }
    
    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public ServerConfig() {
        this.host = "";
        this.port = 0;
        this.workerThreads = 20;
    }
}
