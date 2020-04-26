# Docker

## Commands

```
docker build -t <tag>:<version> . # Build an image from the Dockerfile and tag the image

docker images # List all images

docker rmi [<tag>:<version>]|[<image id>] # Delete image from the local image store

docker run <image>:<tag> # Run specified version of image
docker run sbt-rpm-docker:0.0.1

docker run --name <container name> -p <source>:<destination> -d <image> # -d detached from console
docker run --name sbt-rpm-docker -p 8080:8080 -d sbt-rpm-docker:0.0.1

docker ps -a # List all running containers including stopped ones

docker inspect <container name> # Get additional information
docker inspect sbt-rpm-docker

docker exec -it <container name> bash # Run a shell inside existing container -i make STDIN kept open, -t allocates a pseudo terminal (TTY)
docker exec -it sbt-rpm-docker bash

docker stop <container name> # Stop a container
docker stop sbt-rpm-docker

docker rm <container name> # Remove a container
docker rm sbt-rpm-docker

docker tag <image> <new image tag> # Tag an image under an additional tag
docker tag sbt-rpm-docker sandbox/sbt-rpm-docker

docker push <image tag> # Push image to docker hub
docker push sandbox/sbt-rpm-docker

docker logs <container id> # Get container log

docker save -o sbt-rpm-docker.tar sbt-rpm-docker:0.0.1 # Save image
```

## References
- https://docs.docker.com/engine/reference/commandline/docker/
- https://www.docker.com/sites/default/files/d8/2019-09/docker-cheat-sheet.pdf
