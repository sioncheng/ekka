package entity

import "time"

type EkkaUser struct {
	ID         int64     `gorm:"primary_key;column:id"`
	Username   string    `gorm:"column:username"`
	Passwd     string    `gorm:"column:passwd"`
	PasswdSalt string    `gorm:"column:passwd_salt"`
	Created    time.Time `gorm:"autoCreateTime:milli;column:created;type:datetime"`
	Updated    time.Time `gorm:"autoCreateTime:milli;column:updated;type:datetime"`
}

func (EkkaUser) TableName() string {
	return "ekka_user"
}

var EmptyUser = EkkaUser{}
