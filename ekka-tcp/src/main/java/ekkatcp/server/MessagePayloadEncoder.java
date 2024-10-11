package ekkatcp.server;

public interface MessagePayloadEncoder {
    <T> byte[] encode(byte msgType, T msg);
}
