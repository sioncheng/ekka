package repository

import (
	"testing"
)

func TestInit(t *testing.T) {
	dsn := "pig:123456@tcp(mbp2011:3306)/ekka_usr?charset=utf8mb4&parseTime=True&loc=Local"

	InitDb(dsn)

	if db == nil {
		t.Fatal("db is nil")
	}
}
