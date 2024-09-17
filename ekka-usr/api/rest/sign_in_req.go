package rest

type SignInReq struct {
	Username       string `json:"username" binding:"required"`
	Password       string `json:"password" binding:"required"`
	ExpiresInHours int    `json:"expiresInHours"`
}
