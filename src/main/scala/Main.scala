import akka.actor.{Props, ActorSystem}
import in.jirw.pubsub.PubSubActor
import in.jirw.pubsub.PubSubActor.{Publish, Subscribe}

/**
 * Created by jirwin on 8/24/15.
 */
object Main extends App {
  var system = ActorSystem("pubsub-main")

  val pubSubActor = system.actorOf(Props[PubSubActor], name = "pubsub-actor")

  val publish = (s: String) => println("Got message: " + s)
  pubSubActor ! Subscribe("jirwin", publish)
  pubSubActor ! Subscribe("jirwin", publish)
  pubSubActor ! Subscribe("christina", publish)

  pubSubActor ! Publish("jirwin", "This is to topic jirwin")
  pubSubActor ! Publish("christina", "This is to topic christina")
  pubSubActor ! Publish("foo", "This is to the empty topic foo")
}
