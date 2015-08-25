import akka.actor.{PoisonPill, Props, ActorSystem}
import in.jirw.pubsub.{SubActor, PubSubActor}
import in.jirw.pubsub.PubSubActor.{Unsubscribe, PublishAll, Publish, Subscribe}

/**
 * Created by jirwin on 8/24/15.
 */
object Main extends App {
  var system = ActorSystem("pubsub-main")

  val publish = (s: String) => println("Got message: " + s)
  val publish2 = (s: String) => println("Got a second message: " + s)

  val pubSubActor = system.actorOf(Props[PubSubActor], name = "pubsub-actor")
  val subActor1 = system.actorOf(SubActor.props(publish), name = "subactor-1")
  val subActor2 = system.actorOf(SubActor.props(publish2), name = "subactor-2")


  pubSubActor ! Subscribe("jirwin", subActor1)
  pubSubActor ! Subscribe("jirwin", subActor2)
  pubSubActor ! Subscribe("christina", subActor2)

  pubSubActor ! Publish("jirwin", "This is to topic jirwin")
  pubSubActor ! Publish("christina", "This is to topic christina")
  pubSubActor ! Publish("foo", "This is to the empty topic foo")

  pubSubActor ! PublishAll("This is a message publshed to all topics")
  pubSubActor ! PublishAll("This is another message published to all topics")

  subActor1 ! PoisonPill

  pubSubActor ! Publish("jirwin", "This is a message after subActor1 was killed.")

}
