package ekkatcp.server;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonMessagePayloadDecoder implements MessagePayloadDecoder {

   @Override
   public <T> T decode(byte type, byte[] payload, Class<T> type2) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(payload, type2);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
