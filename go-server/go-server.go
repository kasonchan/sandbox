package main

import (
	"fmt"
	"net/http"
	"os/exec"

	log "github.com/sirupsen/logrus"
)

func main() {
	log.SetFormatter(&log.JSONFormatter{})
	log.SetLevel(log.InfoLevel)

	http.HandleFunc("/", Server)
	http.ListenAndServe(":8080", nil)
}

func Server(w http.ResponseWriter, r *http.Request) {
	out, err := exec.Command("uuidgen").Output()
	if err != nil {
		log.WithFields(log.Fields{
			"path":    r.URL.Path[1:],
			"traceId": err,
		}).Error(err)
		// TODO: Status code 500 due to not able to generate uuid
	}

	if out != nil {
		log.WithFields(log.Fields{
			"path":    r.URL.Path[1:],
			"traceId": out,
		}).Info("Successful")
		fmt.Fprintf(w, "%s", r.URL.Path[1:])
	}
}
