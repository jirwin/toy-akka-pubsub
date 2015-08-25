package in.jirw.pubsub

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}
import in.jirw.pubsub.SubActor.Published

/**
 * Created by jirwin on 8/24/15.
 */

object PubSubActor {
  case class Subscribe(topic: String, a: ActorRef)
  case class Subscribed(sub: Subscribe)
  case class Unsubscribe(topic: String, a: ActorRef)
  case class Unsubscribed(unsub: Unsubscribe)
  case class Publish(topic: String, msg: String)
  case class PublishAll(msg: String)
}

class PubSubActor extends Actor with ActorLogging {
  import PubSubActor._

  private var subscribers = Map.empty[String, Set[ActorRef]].withDefaultValue(Set.empty)

  def receive = {
    case Terminated(corpse) => {
      for ((k, v) <- subscribers) {
        subscribers += (k -> (v - corpse))
      }
    }
    case subscribe @ Subscribe(topic, a) => {
      log.info("Got subscribe message: " + topic)
      subscribers += (topic -> (subscribers(topic) + a))
      context.watch(a)
      sender() ! Subscribed(subscribe)
    }
    case unsubscribe @ Unsubscribe(topic, a) => {
      log.info("Got unsubscribe message: " + topic)
      subscribers += (topic -> (subscribers(topic) - a))
      sender() ! Unsubscribed(unsubscribe)
    }
    case Publish(topic, msg) => {
      for (sub <- subscribers(topic)) sub ! Published(msg)
    }
    case PublishAll(msg) => {
      for ((k, v) <- subscribers) {
        for (sub <- v) sub ! Published(msg)
      }
    }
    case default => log.info("Unknown Message")
  }
}
