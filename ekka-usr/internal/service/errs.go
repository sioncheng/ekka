package service

import "errors"

var (
	ErrSvc                        = errors.New("service error")
	ErrSvcUsernameExists          = errors.New("username exists")
	ErrSvcWrongUsernameOrPassword = errors.New("wrong username or password")
)
