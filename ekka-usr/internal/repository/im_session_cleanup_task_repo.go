package repository

import "github.com/sioncheng/ekka/ekka-usr/internal/repository/entity"

type ImSessionCleanupTaskRepo struct {
}

func NewImSessionCleanupTaskRepo() *ImSessionCleanupTaskRepo {
	return &ImSessionCleanupTaskRepo{}
}

func (p *ImSessionCleanupTaskRepo) List100(id int64) ([]entity.ImSessionCleanupTask, error) {
	var tasks []entity.ImSessionCleanupTask
	ctx := db.Where("id > ? and max_msg_seq > 0", id).Order("id asc").Limit(100).Find(&tasks)
	if ctx.Error != nil {
		return tasks, ctx.Error
	}

	return tasks, nil
}
