import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import com.typesafe.config.ConfigFactory

/**
  * @author kasonchan
  * @since 2018-06
  */
object Master {

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()
    val port = config.getInt("port")
    val system = ActorSystem("ClusterSystem", config)
    system.actorOf(Props[Listener], name = s"master-$port")

    println()
    println(Cluster(system).selfAddress)
    println(Cluster(system).selfDataCenter)
    println(Cluster(system).selfRoles)
    println()
  }

}
