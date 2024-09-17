package internal

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/sioncheng/ekka/ekka-usr/api/rest"
	"github.com/sioncheng/ekka/ekka-usr/internal/repository"
	"github.com/sioncheng/ekka/ekka-usr/internal/service"
)

func NewGinServer() *gin.Engine {
	g := gin.Default()

	g.GET("/health", func(ctx *gin.Context) {
		ctx.JSON(http.StatusOK, "{}")
	})

	//user service
	userRepo := repository.NewEkkaUserRepository()
	userSvc := service.NewUserService(userRepo)
	g.POST("/user/signup", func(ctx *gin.Context) {
		userSignup(ctx, userSvc)
	})
	g.POST("/user/signin", func(ctx *gin.Context) {
		userSignIn(ctx, userSvc)
	})

	return g
}

func userSignup(ctx *gin.Context, userSvc *service.UserService) {
	var req rest.SignUpReq
	if err := ctx.ShouldBind(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{
			"msg": err.Error(),
		})
		return
	}

	res, err := userSvc.SignUp(req)
	if err == service.ErrSvcUsernameExists {
		ctx.JSON(http.StatusBadRequest, gin.H{
			"msg": err.Error(),
		})
	} else if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{
			"msg": err.Error(),
		})
	} else {
		ctx.JSON(http.StatusOK, res)
	}
}

func userSignIn(ctx *gin.Context, userSvc *service.UserService) {
	var req rest.SignInReq
	if err := ctx.ShouldBind(&req); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{
			"msg": err.Error(),
		})
	}

	res, err := userSvc.SignIn(req)
	if err == service.ErrSvcWrongUsernameOrPassword {
		ctx.JSON(http.StatusBadRequest, gin.H{
			"msg": err.Error(),
		})
	} else if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{
			"msg": err.Error(),
		})
	} else {
		ctx.JSON(http.StatusOK, res)
	}
}
