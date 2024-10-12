package main

import (
	"flag"
	"fmt"
	"os"
)

var command string
var payload string

func main() {
	fmt.Println("tcp-conn-main")
	flag.StringVar(&command, "command", "", "command: signin, ping, sendmsg")
	flag.StringVar(&payload, "payload", "", "payload: {}")
	flag.Parse()

	if command == "" || payload == "" {
		flag.Usage()
		os.Exit(1)
	}

	fmt.Println("entered command", command, "payload", payload)
}
