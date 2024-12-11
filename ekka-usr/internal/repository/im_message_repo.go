package repository

import "github.com/sioncheng/ekka/ekka-usr/internal/repository/entity"

type ImMessageRepository struct{}

func NewImMessageRepository() *ImMessageRepository {
	return &ImMessageRepository{}
}

func (p *ImMessageRepository) List100(id string) ([]entity.ImMessage, error) {
	var messages []entity.ImMessage
	ctx := db.Where("id > ? ", id).Order("id asc").Limit(100).Find(&messages)
	if ctx.Error != nil {
		return messages, ctx.Error
	}

	return messages, nil
}

func (p *ImMessageRepository) SetIsDeletedBySessionId(id string) (int64, error) {
	ctx := db.Model(&entity.ImMessage{}).Where("session_id = ? and message_body = '' and is_deleted = 0", id).Update("is_deleted", 1)
	return ctx.RowsAffected, ctx.Error
}
