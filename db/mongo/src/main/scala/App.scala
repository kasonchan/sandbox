import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author kasonchan
  * @since 2020-05
  */
object App {
  def main(args: Array[String]): Unit = {
    val mc = Mongo.mongoClient("127.0.0.1")
    Mongo.dbs(mc).onComplete {
      case Success(s) => println(s.mkString(", "))
      case Failure(f) => println(f.getMessage)
    }
    val db = Mongo.db(mc, "test")
    val c = Mongo.collection(db, "myCollection")
    Mongo.count(c).onComplete {
      case Success(s) => println(s)
      case Failure(f) => println(f.getMessage)
    }
    Thread.sleep(60000)
  }
}
