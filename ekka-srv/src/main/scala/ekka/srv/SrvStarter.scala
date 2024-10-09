package ekka.srv

import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.LoggerFactory

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
    val server = ApiServer(system)

    server.run()

    scala.io.StdIn.readLine("press ENTRY to exit")
  }
}
