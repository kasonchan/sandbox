package com.sandbox.initials

import com.typesafe.config.ConfigFactory

/**
  * Reference link {@link http://typesafehub.github.io/config/latest/api/}
  * @author kasonchan
  * @since Dec-2016
  */
object Configs {

  def main(args: Array[String]): Unit = {
    val applicationConf = ConfigFactory.load("application.conf")
    val value = applicationConf.getString("hello.world.value")
    println(s"hello world value is $value")

    val configsConf = ConfigFactory.load("configs.conf")
    val magicInt = configsConf.getInt("configs.magic.int")
    println(magicInt)

    val magicIntList = configsConf.getIntList("configs.magic.intList")
    println(magicIntList)

    val magicDouble = configsConf.getDouble("configs.magic.double")
    println(magicDouble)

    val magicDoubleList = configsConf.getDoubleList("configs.magic.doubleList")
    println(magicDoubleList)

    val magicWord = configsConf.getString("configs.magic.word")
    println(magicWord)

    val magicBoolean = configsConf.getBoolean("configs.magic.boolean")
    println(magicBoolean)

    val magicBooleanList = configsConf.getBooleanList("configs.magic.booleanList")
    println(magicBooleanList)

    val unknown = configsConf.hasPath("configs.unknown")
    println(unknown)
  }

}
