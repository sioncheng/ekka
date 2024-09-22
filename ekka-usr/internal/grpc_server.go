package internal

import (
	"github.com/sioncheng/ekka/ekka-usr/api/proto"
	"github.com/sioncheng/ekka/ekka-usr/internal/repository"
	"github.com/sioncheng/ekka/ekka-usr/internal/rpc"
	"github.com/sioncheng/ekka/ekka-usr/internal/service"
	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
)

func NewGrpcServer() *grpc.Server {
	server := grpc.NewServer()

	//user service
	userRepo := repository.NewEkkaUserRepository()
	userSvc := service.NewUserService(userRepo)

	reflection.Register(server)
	proto.RegisterSearchServiceServer(server, rpc.NewSearchServiceImpl())
	proto.RegisterUserServiceServer(server, rpc.NewUserServiceImpl(userSvc))
	return server
}
