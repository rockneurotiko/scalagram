package io.rockneurotiko.scalagram.types

import spray.httpx.SprayJsonSupport
import spray.json._

case class KeyboardButton(
  text: String,
  requestContact: Option[Boolean] = None,
  requestLocation: Option[Boolean] = None)

sealed trait ReplyMarkup

case class ReplyKeyboardMarkup(
  keyboard: Seq[Seq[KeyboardButton]],
  resizeKeyboard: Option[Boolean] = None,
  oneTimeKeyboard: Option[Boolean] = None,
  selective: Option[Boolean] = None) extends ReplyMarkup

case class ReplyKeyboardHide(
  hideKeyboard: Boolean,
  selective: Option[Boolean] = None) extends ReplyMarkup

case class ForceReply(
  forceReply: Boolean,
  selective: Option[Boolean] = None) extends ReplyMarkup

case class InlineKeyboardMarkup(
  inlineKeyboard: Seq[Seq[InlineKeyboardButton]])

case class InlineKeyboardButton(
  text: String,
  url: Option[String] = None,
  callbackData: Option[String] = None,
  switchInlineQuery: Option[String] = None)

object ReplyMarkupSupport extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val keyboardButtonFormat = jsonFormat(KeyboardButton, "text", "request_contact", "request_location")
  implicit val replyKeyboardMarkupFormat = jsonFormat(ReplyKeyboardMarkup, "keyboard", "resize_keyboard", "one_time_keyboard", "selective")
  implicit val replyKeyboardHideFormat = jsonFormat(ReplyKeyboardHide, "hide_keyboard", "selective")
  implicit val forceReplyFormat = jsonFormat(ForceReply, "force_reply", "selective")
  implicit val inlineKeyboardButtonFormat = jsonFormat(InlineKeyboardButton, "text", "url", "callback_data", "switch_inline_query")
  implicit val inlineKeyboardMarkupFormat = jsonFormat(InlineKeyboardMarkup, "inline_keyboard")

  implicit object replyMarkupFormat extends RootJsonFormat[ReplyMarkup] {
    def write(r: ReplyMarkup) = r match {
      case rk: ReplyKeyboardMarkup => rk.toJson
      case fr: ForceReply => fr.toJson
      case rk: ReplyKeyboardHide => rk.toJson
      case _ => JsObject()
  }
  def read(v: JsValue) = ForceReply(true)
}
}
