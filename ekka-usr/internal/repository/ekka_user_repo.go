package repository

import (
	"log"

	"github.com/sioncheng/ekka/ekka-usr/internal/repository/entity"
	"gorm.io/gorm"
)

type EkkaUserRepository struct{}

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
		return user, ErrRecordNotFound
	}

	log.Fatal("unexpected error", result)
	return user, result.Error
}
