package io.rockneurotiko.scalagram.types

object MessageEntityTypes {
  sealed trait MessageEntityType

  // case class Mention(username: String) extends MessageEntityType
  // case class TextMention(user: String) extends MessageEntityType
  case object Mention extends MessageEntityType
  case object TextMention extends MessageEntityType
  case object Email extends MessageEntityType
  case object Hashtag extends MessageEntityType
  case object Command extends MessageEntityType
  case object Url extends MessageEntityType
  case object Bold extends MessageEntityType
  case object Italic extends MessageEntityType
  case object Code extends MessageEntityType
  case object Pre extends MessageEntityType
  case object Link extends MessageEntityType

  implicit def messageEntityTypeFromString(msg: String): MessageEntityType = msg match {
    case "mention" => Mention
    case "text_mention" => TextMention
    case "hashtag" => Hashtag
    case "bot_command" => Command
    case "url" => Url
    case "email" => Email
    case "bold" => Bold
    case "italic" => Italic
    case "code" => Code
    case "pre" => Pre
    case "text_link" => Link
    case _ => Mention
  }

  // implicit?
  import spray.json._

  implicit object MessageEntityTypeFormat extends JsonFormat[MessageEntityType] {
    def write(m: MessageEntityType) = JsString("") // Don't need this
    def read(value: JsValue) = {
      val JsString(entity) = value;
      entity
    }
  }
}

import MessageEntityTypes._

case class MessageEntity(
  `type`: MessageEntityType,
  offset: Int,
  length: Int,
  url: Option[String] = None,
  user: Option[User] = None)
