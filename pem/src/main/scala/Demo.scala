import java.io.{FileOutputStream, OutputStreamWriter}
import java.security.KeyPairGenerator

import org.bouncycastle.util.io.pem._

import scala.util.{Failure, Success, Try}

/**
  * @author kasonchan
  * @since 2018-02-27
  */
object Demo {

  def main(args: Array[String]): Unit = {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048)
    val keyPair = keyPairGenerator.generateKeyPair()
    val publicKey = keyPair.getPublic
    val privateKey = keyPair.getPrivate

    println(publicKey)
    println(privateKey)

    val pemObject: PemObject =
      new PemObject("RSA PRIVATE KEY", privateKey.getEncoded)

    println("----- Private key encoded -----")
    println(privateKey.getEncoded.map(x => x.toChar).mkString)

    Try {
      writePem(pemObject, "priv.pem")
    } match {
      case Success(s) => println("Wrote to priv.pem successful")
      case Failure(e) =>
        println(s"Failed written to priv.pem: ${e.getLocalizedMessage}")
    }
  }

  def writePem(pemObject: PemObject, filename: String) = {
    Try {
      new PemWriter(new OutputStreamWriter(new FileOutputStream(filename)))
        .write(pemObject.getContent.map(_.toChar))
    }
  }

}
