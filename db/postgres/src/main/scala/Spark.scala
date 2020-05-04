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
    createSparkSession("db", "local[*]")

  // Read csv data
  // To avoid create case class schema
  val data: DataFrame = ss.read
    .format("csv")
    .option("sep", ",")
    .option("inferSchema", "true")
    .option("header", "true")
    .load("src/main/resources/data-1588432708556.csv")

  def createSparkSession(appName: String, master: String): SparkSession =
    SparkSession.builder
      .appName(appName)
      .master(master)
      .getOrCreate()
}
