package ekkatcp.server;

public interface MessageType {
    static byte SIGNIN_REQ = 1;
    static byte SIGNIN_RES = 2;
    static byte PING = 3;
    static byte PONG = 4;
    static byte SEND_MSG = 5;
    static byte SEND_MSG_ACK = 6;
    static byte NOTI_MSG = 7;
    static byte NOTI_MSG_ACK = 8;
}
