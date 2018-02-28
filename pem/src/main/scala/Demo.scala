import java.io.{File => JFile}
import java.security.{KeyPairGenerator, Security}

import better.files.Dsl._
import org.bouncycastle.jce.provider.BouncyCastleProvider

/**
  * @author kasonchan
  * @since 2018-02-27
  */
object Demo {

  def main(args: Array[String]): Unit = {
    Security.addProvider(new BouncyCastleProvider())
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048)
    val keyPair = keyPairGenerator.generateKeyPair()
    val publicKey = keyPair.getPublic
    val privateKey = keyPair.getPrivate

    import sun.misc.BASE64Encoder
    val encoder = new BASE64Encoder

    println(encoder.encode(privateKey.getEncoded))
    println(encoder.encode(publicKey.getEncoded))

    (pwd / "privateKey.pem")
      .createIfNotExists()
      .overwrite(s"")
      .appendLines("-----BEGIN RSA PRIVATE KEY-----",
                   encoder.encode(privateKey.getEncoded),
                   "-----END RSA PRIVATE KEY-----")

    (pwd / "publicKey.pem")
      .createIfNotExists()
      .overwrite(s"")
      .appendLines("-----BEGIN RSA PUBLIC KEY-----",
                   encoder.encode(publicKey.getEncoded),
                   "-----END RSA PUBLIC KEY-----")
  }

}
