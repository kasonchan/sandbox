# db

## Spark

```
# build.sbt

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.5",
  "org.apache.spark" %% "spark-sql" % "2.4.5"
)
```

### References
- https://docs.mongodb.com/spark-connector/master/scala-api/

## MongoDB

```
# docker

docker pull mongo:latest
docker run --name mongodb -dt -p 127.0.0.1:27017:27017 mongo

# build.sbt

libraryDependencies ++= Seq(
  "org.mongodb.spark" %% "mongo-spark-connector" % "2.4.1",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0"
)
```

In this sandbox, added thread sleep to the end of the app because connection to 
MongoDB take sometimes before it returns to main app.

### References
- http://mongodb.github.io/mongo-scala-driver/2.6/getting-started/
- https://www.programcreek.com/scala/org.mongodb.scala.MongoClient
- http://reactivemongo.org/

## Postgres

```
# docker

docker pull postgres:latest
docker run --name postgresdb -dt -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -v pgdata=/var/lib/postgresql/data/ postgres

```
