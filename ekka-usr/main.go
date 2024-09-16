package main

import (
	"context"
	"log"
	"log/slog"
	"net"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/sioncheng/ekka/ekka-usr/internal"
)

func main() {
	slog.Info("ekka-usr start server ...")

	httpAddr := "0.0.0.0:9090"
	grpcAddr := "0.0.0.0:9091"

	grpcSrv := internal.NewGrpcServer()

	go func() {
		lis, err := net.Listen("tcp", grpcAddr)
		if err != nil {
			log.Fatal("grpc", err)
		}

		slog.Info("ekka-usr grpc serve", "addr", grpcAddr)

		err = grpcSrv.Serve(lis)
		if err != nil {
			log.Fatal("grpc", err)
		}
	}()

	ginSrv := internal.NewGinServer()
	srv := &http.Server{
		Addr:    httpAddr,
		Handler: ginSrv.Handler(),
	}
	go func() {
		slog.Info("ekka-usr gin serve", "addr", httpAddr)

		// service connections
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatal(err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server with
	// a timeout of 5 seconds.
	quit := make(chan os.Signal, 1)
	// kill (no param) default send syscall.SIGTERM
	// kill -2 is syscall.SIGINT
	// kill -9 is syscall. SIGKILL but can"t be catch, so don't need add it
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	slog.Info("ekka-usr shutdown server ...")

	grpcSrv.GracefulStop()
	slog.Info("ekka-usr shutdown grpc")

	ginStop := make(chan interface{})
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := srv.Shutdown(ctx); err != nil {
		log.Fatal(err)
	} else {
		close(ginStop)
	}

	// catching ctx.Done(). timeout of 5 seconds.
	select {
	case <-ctx.Done():
		slog.Info("ekka-usr gin server timeout of 5 seconds")
	case <-ginStop:
		slog.Info("ekka-usr shutdown gin")
	}

	slog.Info("ekka-usr bye")
}
