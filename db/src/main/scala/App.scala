import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * @author kasonchan
  * @since 2020-05
  */
object App {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder
      .appName("db")
      // Remove this in production https://spark.apache.org/docs/latest/submitting-applications.html#master-urls
      .master("local[*]")
      .getOrCreate()

    val data: DataFrame = spark.read
      .format("csv")
      .option("sep", ",")
      .option("inferSchema", "true")
      .option("header", "true")
      .load("src/main/resources/data-1588432647739.csv")
    data.printSchema()
    println(data.columns.mkString(", "))

    spark.stop
  }

  case class Headers(headers: Seq[String]) {
    def getColumnIndex(header: String): Int = headers.indexOf(header)
  }
}
