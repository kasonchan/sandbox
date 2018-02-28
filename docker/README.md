# docker

To publish sbt project build locally:

```
$ sbt docker:publishLocal
```

Create a new container

```
$ docker create sandbox-docker:0.0.1
```

To list all images:

```
$ docker images
```

To list all containers created:

```
$ docker ps -a
```

To start container:

```
$ docker container start <container_name> -i
```
