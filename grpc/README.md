# grpc

This repository is a sandbox playing with grpc.

- [ScalaPB](https://scalapb.github.io/)
- [Akka gRPC](https://doc.akka.io/docs/akka-grpc/current/overview.html)
- [grpclib](https://grpclib.readthedocs.io/en/latest/)

## Scala

### Installation

```
# plugins.sbt
addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "1.0.1")

# build.sbt
enablePlugins(AkkaGrpcPlugin)
```

### Execution

```
$ sbt run
```

## Python

### Installation

```
$ pip3 install grpclib protobuf

$ brew install protobuf

$ protoc --version
libprotoc 3.13.0

$ pip3 install grpcio-tools
$ python3 -m grpc_tools.protoc --version
libprotoc 3.12.2
```

### Execution

```
grpc/grpclib$ python3 -m grpc_tools.protoc -I. --python_out=. --grpclib_python_out=. helloworld.proto

grpc/grpclib$ python3 -m server
Serving on 127.0.0.1:50051

grpc/grpclib$ python3 -m client
```

## References

- https://scalapb.github.io/
- https://doc.akka.io/docs/akka-grpc/current/overview.html
- https://github.com/vmagamedov/grpclib
- https://github.com/grpc/grpc/blob/master/doc/statuscodes.md
- https://www.golinuxcloud.com/openssl-create-client-server-certificate/
