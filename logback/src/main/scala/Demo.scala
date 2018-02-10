import logger.MyLogger

/**
  * @author kasonchan
  * @since Feb-2018
  */
object Demo extends MyLogger {

  def main(args: Array[String]): Unit = {
    log.info("This is a info message")
    log.warn("This is a warning message")
    log.error("This is an error message")
    log.debug("This is a debug message")
    log2.info("This is a info message on log2")
  }

}
