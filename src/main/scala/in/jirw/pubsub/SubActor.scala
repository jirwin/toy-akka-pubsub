package in.jirw.pubsub

import akka.actor.{Props, ActorLogging, Actor}

/**
 * Created by jirwin on 8/24/15.
 */

object SubActor {
  case class Published(msg: String)

  def props(f: String => Unit): Props = Props(new SubActor(f))
}

class SubActor(f: String => Unit) extends Actor with ActorLogging {
  import SubActor._

  def receive = {
    case Published(msg) => f(msg)
  }
}
