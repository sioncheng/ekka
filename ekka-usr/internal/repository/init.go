package repository

import (
	"log"

	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

var db *gorm.DB

func InitDb(dns string) {
	InitDbWithDialet(mysql.Open(dns))
}

func InitDbWithDialet(dialector gorm.Dialector) {
	db1, err := gorm.Open(dialector)
	if err != nil {
		log.Fatal("initdb with dialet", dialector, err)
	}

	db = db1
}
