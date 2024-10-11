package ekka.srv

final object MessageType {
  val SIGNIN_REQ: Byte = 1
  val SIGNIN_RES: Byte = 2
  val PING: Byte = 3
  val PONG: Byte = 4
  val SEND_MSG: Byte = 5
  val SEND_MSG_ACK: Byte = 6
  val NOTI_MSG: Byte = 7
  val NOTI_MSG_ACK: Byte = 8
}
