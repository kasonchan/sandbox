import org.mongodb.scala._

/**
  * @author kasonchan
  * @since 2020-05
  *
  * Print all databases
  * mc.listDatabaseNames.foreach(println)
  */
case object Mongo {
  def mongoClient(host: String): MongoClient = MongoClient(s"mongodb://$host")
}
