package rpc

import (
	"context"

	"github.com/sioncheng/ekka/ekka-usr/api/proto"
	"github.com/sioncheng/ekka/ekka-usr/internal/service"
)

type UserServiceImpl struct {
	svc *service.UserService
	proto.UnimplementedUserServiceServer
}

func NewUserServiceImpl(svc *service.UserService) *UserServiceImpl {
	return &UserServiceImpl{svc: svc}
}

func (p *UserServiceImpl) AuthToken(ctx context.Context, req *proto.AuthTokenReq) (*proto.AuthTokenRes, error) {
	claims, err := service.ParseJwt(req.Token)
	if err != nil {
		return nil, err
	}
	return &proto.AuthTokenRes{Username: claims.Username}, nil
}
