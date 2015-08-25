package in.jirw.pubsub

import akka.actor.{Actor, ActorLogging, ActorRef}
import in.jirw.pubsub.SubActor.Published

/**
 * Created by jirwin on 8/24/15.
 */

object PubSubActor {
  case class Subscribe(topic: String, f: String => Unit)
  case class Publish(topic: String, msg: String)
}

class PubSubActor extends Actor with ActorLogging {
  import PubSubActor._

  private var subscribers = Map.empty[String, Set[ActorRef]].withDefaultValue(Set.empty)

  def receive = {
    case Subscribe(topic, f) => {
      log.info("Got subscribe message: " + topic)
      val child: ActorRef = context.actorOf(SubActor.props(f), name = "subscriber" + topic + subscribers(topic).size)
      subscribers = subscribers + (topic -> (subscribers(topic) + child))
    }
    case Publish(topic, msg) => {
      for (sub <- subscribers(topic)) sub ! Published(msg)
    }
    case default => log.info("Unknown Message")
  }
}
