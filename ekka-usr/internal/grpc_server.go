package internal

import (
	"github.com/sioncheng/ekka/ekka-usr/api/proto"
	"github.com/sioncheng/ekka/ekka-usr/internal/service"
	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
)

func NewGrpcServer() *grpc.Server {
	server := grpc.NewServer()

	reflection.Register(server)
	proto.RegisterSearchServiceServer(server, &service.SearchServiceImpl{})

	return server
}
