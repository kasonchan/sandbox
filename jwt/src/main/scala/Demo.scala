/**
  * @author kasonchan
  * @since Nov-2017
  */
import java.security.{KeyPairGenerator, SecureRandom}

import pdi.jwt.{Jwt, JwtAlgorithm}

import scala.util.Try

/**
  * @author kason.chan
  * @since Nov-2017
  */
object Demo {

  def main(args: Array[String]): Unit = {

    val keyGen = KeyPairGenerator.getInstance("RSA")

    val pair = keyGen.generateKeyPair
    val priv = pair.getPrivate
    val pub = pair.getPublic

    println(priv)
    println(pub)

    val token =
      Jwt.encode("""{"user":1}""", priv, JwtAlgorithm.RS512)
    println(token)

    val decoded =
      Jwt.decodeRawAll(token, pub, Seq(JwtAlgorithm.RS512))
    println(decoded)

    println()
    println()
    println()

    // 2

    val random = SecureRandom.getInstance("SHA1PRNG")

    val keyGen2 = KeyPairGenerator.getInstance("RSA")

    keyGen2.initialize(1024, random)

    val pair2 = keyGen2.generateKeyPair
    val priv2 = pair2.getPrivate
    val pub2 = pair2.getPublic

    println(priv2)
    println(pub2)

    val token2 =
      Jwt.encode("""{"user":1}""", priv2, JwtAlgorithm.RS512)
    println(token2)

    val decoded2 =
      Jwt.decode(token2, pub2, Seq(JwtAlgorithm.RS512))
    println(decoded2)

    println()
    println()
    println()

    // 3

    val random2 = SecureRandom.getInstance("SHA1PRNG")

    val keyGen3 = KeyPairGenerator.getInstance("RSA")

    keyGen3.initialize(2048, random2)

    val pair3 = keyGen3.generateKeyPair
    val priv3 = pair3.getPrivate
    val pub3 = pair3.getPublic

    println(priv3)
    println(pub3)

    val token3: String =
      Jwt.encode("""{"user":1}""", priv3, JwtAlgorithm.RS512)
    println(token3)

    val decoded3: Try[String] =
      Jwt.decode(token3, pub3, Seq(JwtAlgorithm.RS512))
    println(decoded3)
  }

}
