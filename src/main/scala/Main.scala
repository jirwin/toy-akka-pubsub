import akka.actor.{PoisonPill, Props, ActorSystem}
import in.jirw.pubsub.{SubActor, PubSubActor}
import in.jirw.pubsub.PubSubActor.{Unsubscribe, PublishAll, Publish, Subscribe}

import scala.util.Random

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

  val topics = List("jirwin", "foo", "bar", "christina")

  pubSubActor ! Subscribe("jirwin", subActor1)
  pubSubActor ! Subscribe("jirwin", subActor2)
  pubSubActor ! Subscribe("christina", subActor2)

  while(true) {
    val topic = Random.shuffle(topics).head
    pubSubActor ! Publish(topic, "This is a message to a random topic " + topic)
  }
}
