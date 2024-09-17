package service

import (
	"crypto/md5"
	"encoding/hex"
	"time"

	"github.com/golang-jwt/jwt/v5"
)

const JWT_KEY = "8687756c0e2c510833368620f093d776"

type JwtClaims struct {
	Username string
	jwt.RegisteredClaims
}

func MD5(s string) string {
	sum := md5.Sum([]byte(s))
	return hex.EncodeToString(sum[:])
}

func MD5WithSalt(s string, salt string) string {
	return MD5(s + salt)
}

func GenJwt(username string, hours int) (string, error) {
	claims := JwtClaims{
		username,
		jwt.RegisteredClaims{
			ExpiresAt: jwt.NewNumericDate(time.Now().Add(time.Duration(hours) * time.Hour)),
			IssuedAt:  jwt.NewNumericDate(time.Now()),
			NotBefore: jwt.NewNumericDate(time.Now()),
		},
	}

	t := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return t.SignedString([]byte(JWT_KEY))
}

func ParseJwt(tokenString string) (*JwtClaims, error) {
	t, err := jwt.ParseWithClaims(tokenString, &JwtClaims{}, func(token *jwt.Token) (interface{}, error) {
		return []byte(JWT_KEY), nil
	})

	if claims, ok := t.Claims.(*JwtClaims); ok && t.Valid {
		return claims, nil
	} else {
		return nil, err
	}
}
