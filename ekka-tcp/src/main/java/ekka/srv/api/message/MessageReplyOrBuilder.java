// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: message.proto

package ekka.srv.api.message;

public interface MessageReplyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:MessageReply)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string id = 1;</code>
   * @return The id.
   */
  java.lang.String getId();
  /**
   * <code>string id = 1;</code>
   * @return The bytes for id.
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <code>string remote = 2;</code>
   * @return The remote.
   */
  java.lang.String getRemote();
  /**
   * <code>string remote = 2;</code>
   * @return The bytes for remote.
   */
  com.google.protobuf.ByteString
      getRemoteBytes();

  /**
   * <code>int32 messageType = 3;</code>
   * @return The messageType.
   */
  int getMessageType();

  /**
   * <code>bytes messagePayload = 4;</code>
   * @return The messagePayload.
   */
  com.google.protobuf.ByteString getMessagePayload();
}
