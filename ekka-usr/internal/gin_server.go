package internal

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

func NewGinServer() *gin.Engine {
	g := gin.Default()

	g.GET("/health", func(ctx *gin.Context) {
		ctx.JSON(http.StatusOK, "{}")
	})

	return g
}
