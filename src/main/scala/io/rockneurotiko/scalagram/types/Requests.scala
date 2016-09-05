package io.rockneurotiko.scalagram.types

import spray.httpx.SprayJsonSupport
import spray.json._

import ReplyMarkupSupport._
import InlineQueryResultSupport._

// TODO
// ParseMode

trait ReplyToT {
  val replyTo: Option[Long]
}

trait ReplyMarkupT {
  val replyMarkup: Option[ReplyMarkup]
}

trait ReplyInlineKeyboardT {
  val replyMarkup: Option[InlineKeyboardMarkup]
}

sealed trait RequestMessage

case class GetUpdates(
  offset: Option[Int] = None,
  limit: Option[Int] = None,
  timeout: Option[Int] = None) extends RequestMessage

case class SetWebhook(
  url: Option[String] = None,
  certificate: Option[InputFile] = None) extends RequestMessage

case class SendText(
  chatId: Either[String, Long],
  text: String,
  parseMode: Option[String] = None,
  disablePreview: Option[Boolean] = None,
  disableNotification: Option[Boolean] = None,
  replyTo: Option[Long] = None,
  replyMarkup: Option[ReplyMarkup] = None) extends RequestMessage with ReplyToT with ReplyMarkupT

case class ForwardMessage(
  chatId: Either[String, Long],
  fromChatId: Either[String, Long],
  messageId: Long,
  disableNotification: Option[Boolean] = None) extends RequestMessage

case class SendPhoto(
  chatId: Either[String, Long],
  photo: Either[InputFile, String],
  caption: Option[String] = None,
  disableNotification: Option[Boolean] = None,
  replyTo: Option[Long] = None,
  replyMarkup: Option[ReplyMarkup] = None) extends RequestMessage with ReplyToT with ReplyMarkupT

case class SendAudio(
  chatId: Either[String, Long],
  audio: Either[InputFile, String],
  duration: Option[Int] = None,
  performer: Option[String] = None,
  title: Option[String] = None,
  disableNotification: Option[Boolean] = None,
  replyTo: Option[Long] = None,
  replyMarkup: Option[ReplyMarkup] = None) extends RequestMessage with ReplyToT with ReplyMarkupT

case class SendDocument(
  chatId: Either[String, Long],
  document: Either[InputFile, String],
  caption: Option[String] = None,
  disableNotification: Option[Boolean] = None,
  replyTo: Option[Long] = None,
  replyMarkup: Option[ReplyMarkup] = None) extends RequestMessage with ReplyToT with ReplyMarkupT

case class SendSticker(
  chatId: Either[String, Long],
  sticker: Either[InputFile, String],
  disableNotification: Option[Boolean] = None,
  replyTo: Option[Long] = None,
  replyMarkup: Option[ReplyMarkup] = None) extends RequestMessage with ReplyToT with ReplyMarkupT

case class SendVideo(
  chatId: Either[String, Long],
  video: Either[InputFile, String],
  duration: Option[Int] = None,
  width: Option[Int] = None,
  height: Option[Int] = None,
  caption: Option[String] = None,
  disableNotification: Option[Boolean] = None,
  replyTo: Option[Long] = None,
  replyMarkup: Option[ReplyMarkup] = None) extends RequestMessage with ReplyToT with ReplyMarkupT

case class SendVoice(
  chatId: Either[String, Long],
  voice: Either[InputFile, String],
  duration: Option[Int] = None,
  disableNotification: Option[Boolean] = None,
  replyTo: Option[Long] = None,
  replyMarkup: Option[ReplyMarkup] = None) extends RequestMessage with ReplyToT with ReplyMarkupT

case class SendLocation(
  chatId: Either[String, Long],
  latitude: Float,
  longitude: Float,
  replyTo: Option[Long] = None,
  replyMarkup: Option[ReplyMarkup] = None) extends RequestMessage with ReplyToT with ReplyMarkupT

case class SendVenue(
  chatId: Either[String, Long],
  latitude: Float,
  longitude: Float,
  title: String,
  address: String,
  foursquareId: Option[String] = None,
  disableNotification: Option[Boolean] = None,
  replyTo: Option[Long] = None,
  replyMarkup: Option[ReplyMarkup] = None) extends RequestMessage with ReplyToT with ReplyMarkupT

case class SendContact(
  chatId: Either[String, Long],
  phoneNumber: String,
  firstName: String,
  lastName: Option[String] = None,
  disableNotification: Option[Boolean] = None,
  replyTo: Option[Long] = None,
  replyMarkup: Option[ReplyMarkup] = None) extends RequestMessage with ReplyToT with ReplyMarkupT

case class SendChatAction(
  chatId: Either[String, Long],
  action: String) extends RequestMessage

case class GetUserProfilePhotos(
  userId: Long,
  offset: Option[Int] = None,
  limit: Option[Int] = None) extends RequestMessage

case class GetFile(
  fileId: String) extends RequestMessage

case class KickChatMember(
  chatId: Either[String, Long],
  userId: Long) extends RequestMessage

case class LeaveChat(
  chatId: Either[String, Long]) extends RequestMessage

case class UnbanChatMember(
  chatId: Either[String, Long],
  userId: Long) extends RequestMessage

case class GetChat(
  chatId: Either[String, Long]) extends RequestMessage

case class GetChatAdministrators(
  chatId: Either[String, Long]) extends RequestMessage

case class GetChatMembersCount(
  chatId: Either[String, Long]) extends RequestMessage

case class GetChatMember(
  chatId: Either[String, Long],
  userId: Long) extends RequestMessage

// inline

case class AnswerCallbackQuery(
  callbackQueryId: String,
  text: Option[String] = None,
  showAlert: Option[Boolean] = None) extends RequestMessage

case class EditMessageText(
  chatId: Option[Either[String, Long]] = None,
  messageId: Option[Long] = None,
  inlineMessageId: Option[String] = None,
  text: String,
  parseMode: Option[String] = None,
  disablePreview: Option[Boolean] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None) extends RequestMessage with ReplyInlineKeyboardT

case class EditMessageCaption(
  chatId: Option[Either[String, Long]] = None,
  messageId: Option[Long] = None,
  inlineMessageId: Option[String] = None,
  caption: String,
  replyMarkup: Option[InlineKeyboardMarkup] = None) extends RequestMessage with ReplyInlineKeyboardT

case class EditMessageReplyMarkup(
  chatId: Option[Either[String, Long]] = None,
  messageId: Option[Long] = None,
  inlineMessageId: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None) extends RequestMessage with ReplyInlineKeyboardT

case class AnswerInlineQuery(
  inlineQueryId: String,
  results: List[InlineQueryResult],
  cacheTime: Option[Int] = None,
  isPersonal: Option[Boolean] = None,
  nextOffset: Option[String] = None,
  switchPmText: Option[String] = None,
  switchPmParameter: Option[String] = None) extends RequestMessage

object RequestsJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val getUpdatesRequestFormat = jsonFormat(GetUpdates, "offset", "limit", "timeout")
  implicit val sendMessageRequestFormat = jsonFormat(SendText, "chat_id", "text", "parse_mode", "disable_web_page_preview", "disable_notification", "reply_to_message_id", "reply_markup")
  implicit val forwardMessageRequestformat = jsonFormat(ForwardMessage, "chat_id", "from_chat_id", "message_id", "disable_notification")

  implicit object setWebhookRequestFormat extends JsonFormat[SetWebhook] {
    def write(r: SetWebhook) = {
      JsObject(
        "url" -> r.url.toJson
          // certificate can't be sent with JSON
      )
    }
    def read(v: JsValue) = SetWebhook()
  }

  implicit object sendPhotoRequestFormat extends JsonFormat[SendPhoto] {
    def write(r: SendPhoto) = {
      JsObject(
        "chat_id" -> r.chatId.toJson,
        "photo" -> r.photo.right.getOrElse("").toJson,
        "caption" -> r.caption.toJson,
        "disable_notification" -> r.disableNotification.toJson,
        "reply_to_message_id" -> r.replyTo.toJson,
        "reply_markup" -> r.replyMarkup.toJson
      )
    }
    def read(v: JsValue) = SendPhoto(Left(""), Right(""))
  }

  implicit object sendAudioRequestFormat extends JsonFormat[SendAudio] {
    def write(r: SendAudio) = {
      JsObject(
        "chat_id" -> r.chatId.toJson,
        "audio" -> r.audio.right.getOrElse("").toJson,
        "duration" -> r.duration.toJson,
        "performer" -> r.performer.toJson,
        "title" -> r.title.toJson,
        "disable_notification" -> r.disableNotification.toJson,
        "reply_to_message_id" -> r.replyTo.toJson,
        "reply_markup" -> r.replyMarkup.toJson
      )
    }
    def read(v: JsValue) = SendAudio(Left(""), Right(""))
  }

  implicit object sendDocumentRequestFormat extends JsonFormat[SendDocument] {
    def write(r: SendDocument) = {
      JsObject(
        "chat_id" -> r.chatId.toJson,
        "document" -> r.document.right.getOrElse("").toJson,
        "caption" -> r.caption.toJson,
        "disable_notification" -> r.disableNotification.toJson,
        "reply_to_message_id" -> r.replyTo.toJson,
        "reply_markup" -> r.replyMarkup.toJson
      )
    }
    def read(v: JsValue) = SendDocument(Left(""), Right(""))
  }

  implicit object sendStickerRequestFormat extends JsonFormat[SendSticker] {
    def write(r: SendSticker) = {
      JsObject(
        "chat_id" -> r.chatId.toJson,
        "sticker" -> r.sticker.right.getOrElse("").toJson,
        "disable_notification" -> r.disableNotification.toJson,
        "reply_to_message_id" -> r.replyTo.toJson,
        "reply_markup" -> r.replyMarkup.toJson
      )
    }
    def read(v: JsValue) = SendSticker(Left(""), Right(""))
  }

  implicit object sendVideoRequestFormat extends JsonFormat[SendVideo] {
    def write(r: SendVideo) = {
      JsObject(
        "chat_id" -> r.chatId.toJson,
        "video" -> r.video.right.getOrElse("").toJson,
        "duration" -> r.duration.toJson,
        "width" -> r.width.toJson,
        "height" -> r.height.toJson,
        "caption" -> r.caption.toJson,
        "disable_notification" -> r.disableNotification.toJson,
        "reply_to_message_id" -> r.replyTo.toJson,
        "reply_markup" -> r.replyMarkup.toJson
      )
    }
    def read(v: JsValue) = SendVideo(Left(""), Right(""))
  }

  implicit object sendVoiceRequestFormat extends JsonFormat[SendVoice] {
    def write(r: SendVoice) = {
      JsObject(
        "chat_id" -> r.chatId.toJson,
        "voice" -> r.voice.right.getOrElse("").toJson,
        "duration" -> r.duration.toJson,
        "disable_notification" -> r.disableNotification.toJson,
        "reply_to_message_id" -> r.replyTo.toJson,
        "reply_markup" -> r.replyMarkup.toJson
      )
    }
    def read(v: JsValue) = SendVoice(Left(""), Right(""))
  }

  implicit val sendLocationRequestFormat = jsonFormat(SendLocation, "chat_id", "latitude", "longitude", "reply_to_message_id", "reply_markup")
  implicit val sendVenueRequestFormat = jsonFormat(SendVenue, "chat_id", "latitude", "longitude", "title", "address", "foursquare_id", "disable_notification", "reply_to_message_id", "reply_markup")
  implicit val sendContactRequestFormat = jsonFormat(SendContact, "chat_id", "phone_number", "first_name", "last_name", "disable_notification", "reply_to_message_id", "reply_markup")
  implicit val sendChatActionRequestFormat = jsonFormat(SendChatAction, "chat_id", "action")

  implicit val getUserProfilePhotosRequestFormat = jsonFormat(GetUserProfilePhotos, "user_id", "offset", "limit")
  implicit val getFileRequestFormat = jsonFormat(GetFile, "file_id")
  implicit val kickChatMemberRequestFormat = jsonFormat(KickChatMember, "chat_id", "user_id")
  implicit val leaveChatRequestFormat = jsonFormat(LeaveChat, "chat_id")
  implicit val unbanChatMemberRequestFormat = jsonFormat(UnbanChatMember, "chat_id", "user_id")
  implicit val getChatRequestFormat = jsonFormat(GetChat, "chat_id")
  implicit val getChatAdministratorsRequestFormat = jsonFormat(GetChatAdministrators, "chat_id")
  implicit val getChatMembersCountRequestFormat = jsonFormat(GetChatMembersCount, "chat_id")
  implicit val getChatMemberRequestFormat = jsonFormat(GetChatMember, "chat_id", "user_id")
  implicit val answerCallbackQueryRequestFormat = jsonFormat(AnswerCallbackQuery, "callback_query_id", "text", "show_alert")
  implicit val editMessageTextRequestFormat = jsonFormat(EditMessageText, "chat_id", "message_id", "inline_message_id", "text", "parse_mode", "disable_web_page_preview", "reply_markup")
  implicit val editMessageCaptionRequestFormat = jsonFormat(EditMessageCaption, "chat_id", "message_id", "inline_message_id", "caption", "reply_markup")
  implicit val editMessageReplyMarkupRequestFormat = jsonFormat(EditMessageReplyMarkup, "chat_id", "message_id", "inline_message_id", "reply_markup")
  implicit val answerInlineQueryRequestFormat = jsonFormat(AnswerInlineQuery, "inline_query_id", "results", "cache_time", "is_personal", "next_offset", "switch_pm_text", "switch_pm_parameter")

  implicit object requestMessageFormat extends JsonFormat[RequestMessage] {
    def write(r: RequestMessage) = r match {
      case s: GetUpdates => s.toJson
      case s: SetWebhook => s.toJson
      case s: SendText => s.toJson
      case s: ForwardMessage => s.toJson
      case s: SendPhoto => s.toJson
      case s: SendAudio => s.toJson
      case s: SendDocument => s.toJson
      case s: SendSticker => s.toJson
      case s: SendChatAction => s.toJson
      case s: SendLocation => s.toJson
      case s: SendVideo => s.toJson
      case s: SendVoice => s.toJson
      case s: SendVenue => s.toJson
      case s: SendContact => s.toJson
      case s: GetUserProfilePhotos => s.toJson
      case s: GetFile => s.toJson
      case s: KickChatMember => s.toJson
      case s: LeaveChat => s.toJson
      case s: UnbanChatMember => s.toJson
      case s: GetChat => s.toJson
      case s: GetChatAdministrators => s.toJson
      case s: GetChatMembersCount => s.toJson
      case s: GetChatMember => s.toJson
      case s: AnswerCallbackQuery => s.toJson
      case s: EditMessageText => s.toJson
      case s: EditMessageCaption => s.toJson
      case s: EditMessageReplyMarkup => s.toJson
      case s: AnswerInlineQuery => s.toJson
    }
    def read(v: JsValue) = SendText(Left(""), "")
  }
}
