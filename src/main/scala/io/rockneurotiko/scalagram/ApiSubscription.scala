package io.rockneurotiko.scalagram

import Implicits._
import io.rockneurotiko.scalagram.types._
import scala.util.matching._

trait ApiSubscription {
  val uuid = java.util.UUID.randomUUID.toString
  def condition(update: Update): Boolean
  def handle(update: Update): Any
}

class GenericSubscription(innerCondition: Update => Boolean, innerHandler: Update => Any) extends ApiSubscription {
  def condition(update: Update) = innerCondition(update)
  def handle(update: Update) = innerHandler(update)
}

class OneTimeSubscription(api: Api, subs: ApiSubscription) extends ApiSubscription {
  def condition(update: Update) = subs.condition(update)
  def handle(update: Update) = {
    subs.handle(update)
    api.removeSubscription(this)
  }
}

class OncePerSenderSubscription(sub: ApiSubscription) extends ApiSubscription {
  import scala.collection.mutable.Set
  val userIdList: Set[Long] = Set()
  def getId(update: Update): Option[Long] = {
    val list = List(update.message.map(_.sender),
      update.inlineQuery.map(m => m.from.id),
      update.chosenInlineResult.map(m => m.from.id),
      update.callbackQuery.map(m => m.from.id)).flatten
    if (list.size == 0) None
    else Some(list(0))
  }
  def condition(update: Update) = {
    val uid = getId(update);
    !(uid.isDefined && userIdList.contains(uid.get)) && sub.condition(update)
  }
  def handle(update: Update) = {
    sub.handle(update)
    getId(update).foreach(uid => userIdList += uid)
  }
}

object AwesomeHelpers {
  def haveMessage(update: Update) = update.message.isDefined
  def haveEdited(update: Update) = update.editedMessage.isDefined
  def haveInline(update: Update) = update.inlineQuery.isDefined
  def haveChosenResult(update: Update) = update.chosenInlineResult.isDefined
  def haveCallback(update: Update) = update.callbackQuery.isDefined
  def haveReplyMessage(update: Update) = haveMessage(update) && update.message.get.replyTo.isDefined

  def haveText(update: Update) = haveMessage(update) && update.message.get.text.isDefined

  def textSubscription(handler: Message => Any) = new GenericSubscription(
    haveText(_),
    update => update.message.foreach(handler(_)))

  def regexSubscription(reg: Regex, handler: Message => Any) = new GenericSubscription(
    update => haveText(update) &&  reg.findFirstIn(update.message.get.text.get).isDefined,
    update => update.message.foreach(m => reg.findFirstIn(m.text.get) match {
      case Some(_) => handler(m)
      case _ =>
    })
  )

  def replySubscription(chatId: Option[Long] = None, messageId: Option[Long], handler: Message => Any) = new GenericSubscription(
    update => {
      if (haveReplyMessage(update)) {
        if (chatId.isDefined && update.message.get.sender == chatId.get) {
          if (messageId.isDefined && !(update.message.get.replyTo.get.id == messageId.get)) {
            false
          } else {
            true
          }
        }
        true
      } else false
    },
    update => update.message.foreach(handler(_))
  )

  def inlineSubscription(handler: InlineQuery => Any) = new GenericSubscription(
    haveInline(_),
    update => update.inlineQuery.foreach(handler(_))
  )

  def inlineSubscriptionRegex(reg: Regex, handler: InlineQuery => Any) = new GenericSubscription(
    update => haveInline(update) && reg.findFirstIn(update.inlineQuery.get.query).isDefined,
    update => update.inlineQuery.foreach(in => reg.findFirstIn(in.query).foreach(_ => handler(in)))
  )
}

trait Awesome {
  this: Api =>

  import AwesomeHelpers._

  def on(handler: Update => Any) = {
    addSubscription(new GenericSubscription(_ => true, handler(_)))
  }

  def onText(handler: Message => Any) = {
    addSubscription(textSubscription(handler))
  }

  def onRegex(reg: Regex)(handler: Message => Any) = {
    addSubscription(regexSubscription(reg, handler))
  }

  def onCommand(cmd: String)(handler: Message => Any) = if (cmd.contains(" ")) {
    // If spaces, don't create subscription and print a warning
    println(s"WARNING: Command can't have spaces. Command was: ${cmd}")
  } else {
    onRegex(s"""^/${cmd}(@.+)?""".r)(handler)
  }

  def onReply(chatId: Option[Either[Long, Message]] = None, messageId: Option[Either[Long, Message]] = None)(handler: Message => Any) = {
    addSubscription(replySubscription(
      chatId.map(_.fold(l => l, m => m.sender)),
      messageId.map(_.fold(l => l, m => m.id)),
      handler))
  }

  def onReplyTo(msg: Message, handler: Message => Any)  = {
    addSubscription(replySubscription(Some(msg.id), None, handler))
  }

  def onInline(handler: InlineQuery => Any) = {
    addSubscription(inlineSubscription(handler))
  }

  def onInlineRegex(reg: Regex)(handler: InlineQuery => Any) = {
    addSubscription(inlineSubscriptionRegex(reg, handler))
  }

  def oneTime(sub: ApiSubscription) = {
    removeSubscription(sub) // Just in case
    addSubscription(new OneTimeSubscription(this, sub))
  }
}
