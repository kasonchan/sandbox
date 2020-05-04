import java.util.Properties

import org.apache.spark.sql.{DataFrame, SaveMode}

/**
  * @author kasonchan
  * @since 2020-05
  */
object App {
  def main(args: Array[String]): Unit = {
    val url: String = s"jdbc:postgresql://127.0.0.1/test"
    val tableName = "myCollection"
    val properties = new Properties()
    properties.setProperty("user", "postgres")
    properties.setProperty("password", "postgres")
    properties.put("driver", "org.postgresql.Driver")
    // Write data to postgres db
    Spark.data.write.mode(SaveMode.Ignore).jdbc(url, tableName, properties)

    // Read data from postgres db
    val data: DataFrame = Spark.ss.read.jdbc(url, tableName, properties)
    data.printSchema
    println(data.count)
    data.show()
    Spark.ss.close
  }
}
