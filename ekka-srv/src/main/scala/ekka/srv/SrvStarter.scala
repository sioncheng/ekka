package ekka.srv

import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.LoggerFactory
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.sharding.typed.scaladsl.Entity
import ekka.srv.cluster.MessageProtocol
import ekka.srv.cluster.RemoteClient

object SrvStarter {

  def main(args: Array[String]): Unit = {

    val log = LoggerFactory.getLogger("SrvStarter")

    log.info("SrvStarter#main")

    val conf = ConfigFactory
      .parseString("""
            akka.http.server.enable-http2 = on
        """)
      .withFallback(ConfigFactory.load("application.conf"))

        val system = ActorSystem[Nothing](Behaviors.empty, "SrvServer", conf)

        val sharding = ClusterSharding(system)
        var shardingRegion = sharding.init(Entity(MessageProtocol.RemoteClientTypeKey)(ctx => RemoteClient(ctx.entityId)))

        val server = ApiServer(system, sharding, shardingRegion)

    server.run()

    scala.io.StdIn.readLine("press ENTRY to exit")
  }
}
