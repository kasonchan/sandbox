import org.apache.spark.sql.DataFrame

/**
  * @author kasonchan
  * @since 2020-05
  */
object App {
  def main(args: Array[String]): Unit = {
    // Write to Mongodb
    Spark.writeData(Spark.data)

    // Read new data from Mongodb
    val r: DataFrame = Spark.readData(Spark.ss)
    r.printSchema

    println(r.columns.mkString("|"))
    println(r.count)

    Spark.ss.stop
  }
}
