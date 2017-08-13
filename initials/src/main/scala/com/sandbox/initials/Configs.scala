package com.sandbox.initials

import com.typesafe.config.ConfigFactory

/**
  * @author kasonchan
  * @since Dec-2016
  */
object Configs {

  def main(args: Array[String]): Unit = {
    // Default to parse application.conf
    val applicationConf = ConfigFactory.load()
    val value = applicationConf.getString("sandbox.hello.world.value")
    println(s"hello world value is $value")

    // Specify the configuration file and read different types of values
    val configsConf = ConfigFactory.load("configs.conf")
    val sandbox = configsConf.getConfig("sandbox")

    val magicInt = sandbox.getInt("configs.magic.int")
    println(magicInt)

    val magicIntList = sandbox.getIntList("configs.magic.intList")
    println(magicIntList)

    val magicDouble = sandbox.getDouble("configs.magic.double")
    println(magicDouble)

    val magicDoubleList = sandbox.getDoubleList("configs.magic.doubleList")
    println(magicDoubleList)

    val magicWord = sandbox.getString("configs.magic.word")
    println(magicWord)

    val magicBoolean = sandbox.getBoolean("configs.magic.boolean")
    println(magicBoolean)

    val magicBooleanList = sandbox.getBooleanList("configs.magic.booleanList")
    println(magicBooleanList)

    val unknown = sandbox.hasPath("sandbox.configs.unknown")
    println(unknown)

    // Specify the configuration file and uses fallback
    val configsProperties = ConfigFactory.load("configs.properties")
    val prop = configsConf.getConfig("sandbox").withFallback(configsProperties).getString("sandbox.configs.prop")
    println(prop)

    // Specify the configuration file and uses fallback
    val configsJson = ConfigFactory.load("configs.json")
    val json = configsConf.getConfig("sandbox").withFallback(configsJson).getString("sandbox.configs.json")
    println(json)

    // Use configuration factory to parse a string
    val parsedString = ConfigFactory.parseString("a.b=12")
    println(parsedString)
  }

}
