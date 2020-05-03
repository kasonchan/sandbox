import org.mongodb.scala._

import scala.concurrent.Future

/**
  * @author kasonchan
  * @since 2020-05
  *
  * Need to allow connection to mongodb, therefore sleep for sometimes
  * import scala.concurrent.ExecutionContext.Implicits.global
  *
  * Need to close client connection after usage
  * mc.close()
  *
  * Build library dependencies only
  * "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0"
  *
  * References
  * http://mongodb.github.io/mongo-scala-driver/2.6/getting-started/
  */
case object Mongo {
  def mongoClient(host: String): MongoClient = MongoClient(s"mongodb://$host")

  def dbs(mc: MongoClient): Future[Seq[String]] =
    mc.listDatabaseNames().toFuture()

  def db(mc: MongoClient, name: String): MongoDatabase = mc.getDatabase(name)

  def collection(db: MongoDatabase, name: String): MongoCollection[Document] =
    db.getCollection("myCollection")

  def count(c: MongoCollection[Document]) = c.countDocuments().toFuture()
}
