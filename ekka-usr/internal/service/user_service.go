package service

import (
	"fmt"
	"math/rand"
	"strings"
	"time"

	"github.com/sioncheng/ekka/ekka-usr/api/rest"
	"github.com/sioncheng/ekka/ekka-usr/internal/repository"
	"github.com/sioncheng/ekka/ekka-usr/internal/repository/entity"
)

var rnd = rand.New(rand.NewSource(time.Now().UnixNano()))

type UserService struct {
	repo *repository.EkkaUserRepository
}

func NewUserService(rep *repository.EkkaUserRepository) *UserService {
	return &UserService{repo: rep}
}

func (p *UserService) SignUp(req rest.SignUpReq) (rest.SignUpRes, error) {
	u, e := p.repo.FindUserByUsername(req.Username)
	if e != nil && e != repository.ErrRepoRecordNotFound {
		return rest.SignUpRes{}, e
	}
	if u.ID > 0 {
		return rest.SignUpRes{}, ErrSvcUsernameExists
	}

	salt := fmt.Sprintf("salt%d", rnd.Intn(10000))
	passwd := MD5WithSalt(req.Password, salt)
	user := entity.EkkaUser{
		Username:   req.Username,
		Passwd:     passwd,
		PasswdSalt: salt,
		Created:    time.Now(),
		Updated:    time.Now(),
	}

	_, err := p.repo.CreateUser(&user)
	if err != nil {
		if strings.Contains(err.Error(), "Duplicate") || strings.Contains(err.Error(), "duplicate") {
			return rest.SignUpRes{}, ErrSvcUsernameExists
		} else {
			return rest.SignUpRes{}, err
		}
	}

	return rest.SignUpRes{Username: user.Username}, nil
}

func (p *UserService) SignIn(req rest.SignInReq) (rest.SignInRes, error) {
	u, e := p.repo.FindUserByUsername(req.Username)
	if e != nil {
		if e == repository.ErrRepoRecordNotFound {
			return rest.SignInRes{}, ErrSvcWrongUsernameOrPassword
		} else {
			return rest.SignInRes{}, e
		}
	}

	md5 := MD5WithSalt(req.Password, u.PasswdSalt)
	if md5 == u.Passwd {
		return rest.SignInRes{Username: req.Username}, nil
	} else {
		return rest.SignInRes{}, ErrSvcWrongUsernameOrPassword
	}
}
