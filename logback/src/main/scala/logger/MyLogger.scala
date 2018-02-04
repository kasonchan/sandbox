package logger

import org.slf4j.{Logger, LoggerFactory}

/**
  * @author kasonchan
  * @since Feb-2018
  */
trait MyLogger {
  val log: Logger = LoggerFactory.getLogger(this.getClass)
}
