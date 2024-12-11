package entity

type ImMessage struct {
	ID          string `gorm:"primary_key;column:id"`
	MessageBody string `grom:"column:message_body"`
	SessionType int32  `grom:"column:session_type"`
	IsDeleted   int32  `grom:"column:is_deleted"`
}

func (ImMessage) TableName() string {
	return "im_message"
}
