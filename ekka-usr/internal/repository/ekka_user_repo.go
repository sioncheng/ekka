package repository

import (
	"github.com/sioncheng/ekka/ekka-usr/internal/repository/entity"
	"gorm.io/gorm"
)

type EkkaUserRepository struct{}

func NewEkkaUserRepository() *EkkaUserRepository {
	return &EkkaUserRepository{}
}

func (p *EkkaUserRepository) CreateUser(user *entity.EkkaUser) (int64, error) {
	result := db.Create(user)
	return result.RowsAffected, result.Error
}

func (p *EkkaUserRepository) FindUserByUsername(username string) (entity.EkkaUser, error) {
	var user entity.EkkaUser
	result := db.First(&user, "username", username)
	if result.Error == nil {
		return user, nil
	}

	if result.Error == gorm.ErrRecordNotFound {
		return user, ErrRepoRecordNotFound
	}

	return user, result.Error
}
