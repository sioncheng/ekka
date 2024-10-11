package ekkatcp.server;

public interface MessagePayloadDecoder {

  <T> T decode(byte type, byte[] payload, Class<T> type2);
}