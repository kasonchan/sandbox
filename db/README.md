# db

## Spark

```
# build.sbt

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.5",
  "org.apache.spark" %% "spark-sql" % "2.4.5"
)
```

## MongoDB

```
docker pull mongo:latest
docker run -name mongo -it -d mongo
```

## Postgres


