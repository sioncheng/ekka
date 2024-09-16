package repository

import (
	"database/sql"
	"log"
	"testing"
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/sioncheng/ekka/ekka-usr/internal/repository/entity"
	"github.com/stretchr/testify/assert"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

func TestFindByUsernameWithUsernameNotExists(t *testing.T) {
	db, mock := initDbMock(t)
	defer db.Close()

	username := "usernotexists"
	eq := mock.ExpectQuery("SELECT")
	eq.WithArgs(username, 1)
	eq.WillReturnError(gorm.ErrRecordNotFound)

	repo := &EkkaUserRepository{}
	user, err := repo.FindUserByUsername(username)
	if err != gorm.ErrRecordNotFound {
		log.Fatal(err)
	}
	if user.Id != 0 {
		log.Fatal(user)
	}
}

func TestFindByUsernameWithUsernameExists(t *testing.T) {
	db, mock := initDbMock(t)
	defer db.Close()

	username := "userexists"

	rows := sqlmock.NewRows([]string{"id", "username"})
	rows.AddRow(1, username)

	eq := mock.ExpectQuery("SELECT")
	eq.WithArgs(username, 1)
	eq.WillReturnRows(rows)

	repo := &EkkaUserRepository{}
	user, err := repo.FindUserByUsername(username)
	if err != nil {
		log.Fatal(err)
	}
	if user.Id != 1 {
		log.Fatal(user)
	}

	t.Log("user:", user)
}

func TestCreateUser(t *testing.T) {
	db, mock := initDbMock(t)
	defer db.Close()

	user := entity.EkkaUser{Username: "u1",
		Passwd:     "p1",
		PasswdSalt: "p10abc",
		Created:    time.Now(),
		Updated:    time.Now(),
	}

	mock.ExpectBegin()
	eq := mock.ExpectExec("INSERT")
	eq.WillReturnResult(sqlmock.NewResult(1, 1))
	mock.ExpectCommit()

	repo := EkkaUserRepository{}
	n, err := repo.CreateUser(&user)
	assert.Equal(t, nil, err)
	assert.Equal(t, int64(1), n)
	assert.Equal(t, int64(1), user.Id)

	t.Log(user, eq)
}

func initDbMock(t *testing.T) (*sql.DB, sqlmock.Sqlmock) {
	db, mock, err := sqlmock.New()
	if err != nil {
		t.Fatal(err)
	}

	mysqlConf := mysql.Config{
		Conn:                      db,
		SkipInitializeWithVersion: true,
	}
	dialector := mysql.New(mysqlConf)

	InitDbWithDialet(dialector)

	return db, mock
}
