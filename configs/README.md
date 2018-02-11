# sandbox-configs

## Package service as rpm

```
$ sbt clean rpm:packageBin
```

## Create service directory and copy rpm to the directory

```
$ mkdir service
$ cp sandbox-configs-0.0.1-1.noarch.rpm service
```

## Unpack rpm without installation

```
service$ rpm2cpio sandbox-configs-0.0.1-1.noarch.rpm | cpio -idmv
```

## Move files to current directory

```
service$ mv -v usr/share/sandbox-configs/* .
```

## Start service

```
service$ scripts/start.sh
```
