package service

import (
	"context"

	"github.com/sioncheng/ekka/ekka-usr/api/proto"
)

type SearchServiceImpl struct {
	proto.UnimplementedSearchServiceServer
}

func (p *SearchServiceImpl) Search(ctx context.Context, req *proto.SearchRequest) (*proto.SearchResponse, error) {
	return &proto.SearchResponse{Response: "Response for " + req.GetRequest()}, nil
}
