package service

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestJwt(t *testing.T) {
	tokenString, err := GenJwt("username", 1)
	if err != nil {
		t.Fatal(err)
	}

	claims, err := ParseJwt(tokenString)
	if err != nil {
		t.Fatal(err)
	}

	assert.Equal(t, "username", claims.Username)
}
