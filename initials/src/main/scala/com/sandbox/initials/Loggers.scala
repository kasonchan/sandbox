package com.sandbox.initials

import com.typesafe.scalalogging.LazyLogging

/**
  * @author kasonchan
  * @since Dec-2016
  */
object Loggers extends LazyLogging {

  def main(args: Array[String]): Unit = {
    logger.info("This is a normal message.")

    logger.warn("This is a warning message.")

    logger.debug("This is a debugging message.")

    logger.error("This is an error message.")
  }

}
