package entity

type ImSessionCleanupTask struct {
	ID        int64  `gorm:"primary_key;column:id"`
	SessionId string `gorm:"column:session_id"`
}

func (ImSessionCleanupTask) TableName() string {
	return "im_session_cleanup_task"
}
