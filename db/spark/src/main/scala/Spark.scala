import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * @author kasonchan
  * @since 2020-05
  *
  * Print schema
  * <dataframe>.printSchema
  *
  * Print schema columns
  * println(<dataframe>.columns.mkString(", ")
  *
  * Stop spark session
  * <spark session>.stop
  *
  * References
  * https://docs.mongodb.com/spark-connector/master/scala-api/
  */
object Spark {
  val ss: SparkSession =
    createSparkSession("db", "127.0.0.1", "test", "myCollection", "local[*]")

  // Read csv data
  // To avoid create case class schema
  val data: DataFrame = ss.read
    .format("csv")
    .option("sep", ",")
    .option("inferSchema", "true")
    .option("header", "true")
    .load("src/main/resources/data-1588432647739.csv")

  // Write to Mongodb
  def writeData(data: DataFrame) = MongoSpark.save(data)

  // Read new data from Mongodb
  def readData(ss: SparkSession): DataFrame = MongoSpark.load(ss)

  def createSparkSession(appName: String,
                         host: String,
                         db: String,
                         collection: String,
                         master: String): SparkSession =
    SparkSession.builder
      .appName(appName)
      // Remove this in production https://spark.apache.org/docs/latest/submitting-applications.html#master-urls
      .config("spark.mongodb.input.uri", s"mongodb://$host/$db.$collection")
      .config("spark.mongodb.output.uri", s"mongodb://$host/$db.$collection")
      .master(master)
      .getOrCreate()
}
