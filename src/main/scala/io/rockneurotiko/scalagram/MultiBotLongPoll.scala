package io.rockneurotiko.scalagram

import io.rockneurotiko.scalagram.types._
import Implicits._

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

import scala.concurrent.ExecutionContext.Implicits.global

sealed trait BotMessages

case object Start extends BotMessages
case object Update extends BotMessages
case object FinishUpdate extends BotMessages

class MultiBotActor(bot: Api) extends Actor {
  var offset = 0

  def handleUpdates(sender: ActorRef) = {
    val gu = GetUpdates(offset, 100, 30)
    bot.getUpdates(gu).flatMap(updates => {
      // println("I HAVE UPDATES")
      offset = bot.getOffset(updates, offset)
      bot.syncFutureHandler(updates)
    }).foreach(_ => sender ! FinishUpdate)
  }

  def receive = {
    case Update => {
      // println("UPDATE")
      handleUpdates(sender)
    }
    case other => println(other)
  }
}
object MultiBotActor {
  def props(bot: Api): Props = Props(new MultiBotActor(bot))
}

class MultiBotSupervisor(bots: List[Api]) extends Actor {
  def receive = {
    case Start => {
      println("START BOT SUPERVISOR!")
      bots.foreach(b => {
        // b.getMe().foreach(me => println(me))
        println("BOT STARTED")
        val bot = context.actorOf(MultiBotActor.props(b), s"bot::${b.token}")
        bot ! Update // Let's start the updates!
      })
      println("FINISH START")
    }
    case FinishUpdate => {
      // println("Finish, let's update again!")
      // Update again!
      sender ! Update
    }
    case other => println(other)
  }
}
object MultiBotSupervisor {
  def props(bots: List[Api]): Props = Props(new MultiBotSupervisor(bots))
}

object MultiBotLongPoll {
  def run(bots: Api*) (implicit system: ActorSystem) = {
    val supervisor = system.actorOf(MultiBotSupervisor.props(bots.toList), "supervisor")
    supervisor ! Start
  }
}
