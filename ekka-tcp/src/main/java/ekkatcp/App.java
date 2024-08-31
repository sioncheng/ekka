package ekkatcp;

import ekkatcp.server.MessageProcessorConfig;
import ekkatcp.server.ServerConfig;

public class App {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private ServerConfig server;

    public ServerConfig getServer() {
        return server;
    }

    public void setServer(ServerConfig server) {
        this.server = server;
    }

    private MessageProcessorConfig messageProcessor;

    public MessageProcessorConfig getMessageProcessor() {
        return messageProcessor;
    }

    public void setMessageProcessor(MessageProcessorConfig messageProcessor) {
        this.messageProcessor = messageProcessor;
    }
}
