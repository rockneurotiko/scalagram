package io.rockneurotiko.scalagram.types

import spray.httpx.SprayJsonSupport
import spray.json._
import InputMessageContentSupport._
import ReplyMarkupSupport._

object InlineQueryTypes {
  sealed trait InlineQueryType

  case object Article extends InlineQueryType
  case object Audio extends InlineQueryType
  case object Contact extends InlineQueryType
  case object Document extends InlineQueryType
  case object Gif extends InlineQueryType
  case object Location extends InlineQueryType
  case object Mpeg4Gif extends InlineQueryType
  case object Photo extends InlineQueryType
  case object Sticker extends InlineQueryType
  case object Venue extends InlineQueryType
  case object Video extends InlineQueryType
  case object Voice extends InlineQueryType
  // implicit?

  implicit def inlineQueryTypeToStr(q: InlineQueryType): String = q match {
    case Article => "article"
    case Audio => "audio"
    case Contact => "contact"
    case Document => "document"
    case Gif => "gif"
    case Location => "location"
    case Mpeg4Gif => "mpeg4_gif"
    case Photo => "photo"
    case Sticker => "sticker"
    case Video => "video"
    case Venue => "venue"
    case Voice => "voice"
  }

  implicit def inlineQueryTypeFromStr(q: String): InlineQueryType = q match {
    case "article" => Article
    case "audio" => Audio
    case "contact" => Contact
    case "document" => Document
    case "gif" => Gif
    case "location" => Location
    case "mpeg4_gif" => Mpeg4Gif
    case "photo" => Photo
    case "sticker" => Sticker
    case "video" => Video
    case "venue" => Venue
    case "voice" => Voice
  }
  implicit object inlineQueryTypeFormat extends JsonFormat[InlineQueryType] {
    def write(i: InlineQueryType) = JsString(i)
    def read(v: JsValue) = {
      val JsString(s) = v
      s
    }
  }
}

import InlineQueryTypes._

sealed trait InlineQueryResult {
  def id: String
  def `type`: InlineQueryType
  def replyMarkup: Option[InlineKeyboardMarkup]
}

case class InlineQueryResultArticle(
  id: String,
  title: String,
  inputMessageContent: InputMessageContent,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  url: Option[String] = None,
  hideUrl: Option[Boolean] = None,
  description: Option[String] = None,
  thumbUrl: Option[String] = None,
  thumbWidth: Option[Int] = None,
  thumbHeight: Option[Int] = None,
  `type`: InlineQueryType = InlineQueryTypes.Article
) extends InlineQueryResult

case class InlineQueryResultPhoto(
  id: String,
  photoUrl: String,
  thumbUrl: String,
  photoWidth: Option[Int] = None,
  photoHeight: Option[Int] = None,
  title: Option[String] = None,
  description: Option[String] = None,
  caption: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType =InlineQueryTypes.Photo
) extends InlineQueryResult

case class InlineQueryResultGif(
  id: String,
  gifUrl: String,
  gifWidth: Option[Int] = None,
  gifHeight: Option[Int] = None,
  thumbUrl: String,
  title: Option[String] = None,
  caption: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Gif
) extends InlineQueryResult

case class InlineQueryResultMpeg4Gif(
  id: String,
  mpeg4Url: String,
  mpeg4Width: Option[Int] = None,
  mpeg4Height: Option[Int] = None,
  thumbUrl: String,
  title: Option[String] = None,
  caption: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Mpeg4Gif
) extends InlineQueryResult

case class InlineQueryResultVideo(
  id: String,
  videoUrl: String,
  mimeType: String,
  thumbUrl: String,
  title: Option[String] = None,
  caption: Option[String] = None,
  videoWidth: Option[Int] = None,
  videoHeight: Option[Int] = None,
  videoDuration: Option[Int] = None,
  description: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Video
) extends InlineQueryResult

case class InlineQueryResultAudio(
  id: String,
  audioUrl: String,
  title: Option[String] = None,
  performer: Option[String] = None,
  audioDuration: Option[Int] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Audio
) extends InlineQueryResult

case class InlineQueryResultVoice(
  id: String,
  voiceUrl: String,
  title: Option[String] = None,
  voiceDuration: Option[Int] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Voice
) extends InlineQueryResult

case class InlineQueryResultDocument(
  id: String,
  title: String,
  caption: Option[String] = None,
  documentUrl: Option[String] = None,
  mimeType: String, // either “application/pdf” or “application/zip”
  description: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  thumbUrl: Option[String] = None,
  thumbWidth: Option[Int] = None,
  thumbHeight: Option[Int] = None,
  `type`: InlineQueryType = InlineQueryTypes.Document
) extends InlineQueryResult

case class InlineQueryResultLocation(
  id: String,
  latitude: Double,
  longitude: Double,
  title: String,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  thumbUrl: Option[String] = None,
  thumbWidth: Option[Int] = None,
  thumbHeight: Option[Int] = None,
  `type`: InlineQueryType = InlineQueryTypes.Location
) extends InlineQueryResult

case class InlineQueryResultVenue(
  id: String,
  latitude: Double,
  longitude: Double,
  title: String,
  address: String,
  foursquareId: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  thumbUrl: Option[String] = None,
  thumbWidth: Option[Int] = None,
  thumbHeight: Option[Int] = None,
  `type`: InlineQueryType = InlineQueryTypes.Venue
) extends InlineQueryResult

case class InlineQueryResultContact(
  id: String,
  phoneNumber: String,
  firstName: String,
  lastName: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  thumbUrl: Option[String] = None,
  thumbWidth: Option[Int] = None,
  thumbHeight: Option[Int] = None,
  `type`: InlineQueryType = InlineQueryTypes.Contact
) extends InlineQueryResult

case class InlineQueryResultCachedPhoto(
  id: String,
  photoFileId: String,
  title: Option[String] = None,
  description: Option[String] = None,
  caption: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Photo
) extends InlineQueryResult

case class InlineQueryResultCachedGif(
  id: String,
  gifFileId: String,
  title: Option[String] = None,
  caption: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Gif
) extends InlineQueryResult

case class InlineQueryResultCachedMpeg4Gif(
  id: String,
  mpeg4FileId: String,
  title: Option[String] = None,
  caption: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Mpeg4Gif
) extends InlineQueryResult

case class InlineQueryResultCachedSticker(
  id: String,
  stickerFileId: String,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Sticker
) extends InlineQueryResult

case class InlineQueryResultCachedDocument(
  id: String,
  title: String,
  documentFileId: String,
  description: Option[String] = None,
  caption: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Document
) extends InlineQueryResult

case class InlineQueryResultCachedVideo(
  id: String,
  videoFileId: String,
  title: String,
  description: Option[String] = None,
  caption: Option[String] = None,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Video
) extends InlineQueryResult

case class InlineQueryResultCachedVoice(
  id: String,
  voiceFileId: String,
  title: String,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Voice
) extends InlineQueryResult

case class InlineQueryResultCachedAudio(
  id: String,
  audioFileId: String,
  replyMarkup: Option[InlineKeyboardMarkup] = None,
  inputMessageContent: Option[InputMessageContent] = None,
  `type`: InlineQueryType = InlineQueryTypes.Audio
) extends InlineQueryResult

object InlineQueryResultSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val inlineQueryResultArticleFormat = jsonFormat(InlineQueryResultArticle, "id", "title", "input_message_content", "reply_markup", "url", "hide_url", "description", "thumb_url", "thumb_width", "thumb_height", "type")
  implicit val inlineQueryResultPhotoFormat = jsonFormat(InlineQueryResultPhoto, "id", "photo_url", "thumb_url", "photo_width", "photo_height", "title", "description", "caption", "replyMarkup", "inputMessageContent", "type")
  implicit val inlineQueryResultGifFormat = jsonFormat(InlineQueryResultGif, "id", "gif_url", "gif_width", "gif_height", "thumb_url", "title", "caption", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultMpeg4GifFormat = jsonFormat(InlineQueryResultMpeg4Gif, "id", "mpeg4_url", "mpeg4_width", "mpeg4_height", "thumb_url", "title", "caption", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultVideoFormat = jsonFormat(InlineQueryResultVideo, "id", "video_url", "mime_type", "thumb_url", "title", "caption", "video_width", "video_height", "video_duration", "description", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultAudioFormat = jsonFormat(InlineQueryResultAudio, "id", "audio_url", "title", "performer", "audio_duration", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultVoiceFormat = jsonFormat(InlineQueryResultVoice, "id", "voice_url", "title", "voice_duration", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultDocumentFormat = jsonFormat(InlineQueryResultDocument, "id", "title", "caption", "document_url", "mimeType", "description", "reply_markup", "input_message_content", "thumb_url", "thumb_width", "thumb_height", "type")
  implicit val inlineQueryResultLocationFormat = jsonFormat(InlineQueryResultLocation, "id", "latitude", "longitude", "title", "reply_markup", "input_message_content", "thumb_url", "thumb_width", "thumb_height", "type")
  implicit val inlineQueryResultVenueFormat = jsonFormat(InlineQueryResultVenue, "id", "latitude", "longitude", "title", "address", "foursquare_id", "reply_markup", "input_message_content", "thumb_url", "thumb_width", "thumb_height", "type")
  implicit val inlineQueryResultContactFormat = jsonFormat(InlineQueryResultContact, "id", "phone_number", "first_name", "last_name", "reply_markup", "input_message_content", "thumb_url", "thumb_width", "thumb_height", "type")

  // Cached
  implicit val inlineQueryResultCachedPhotoFormat = jsonFormat(InlineQueryResultCachedPhoto, "id", "photo_file_id", "title", "description", "caption", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultCachedGifFormat = jsonFormat(InlineQueryResultCachedGif, "id", "gif_file_id", "title", "caption", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultCachedMpeg4GifFormat = jsonFormat(InlineQueryResultCachedMpeg4Gif, "id", "mpeg4_file_id", "title", "caption", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultCachedStickerFormat = jsonFormat(InlineQueryResultCachedSticker, "id", "sticker_file_id", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultCachedDocumentFormat = jsonFormat(InlineQueryResultCachedDocument, "id", "title", "document_file_id", "description", "caption", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultCachedVideoFormat = jsonFormat(InlineQueryResultCachedVideo, "id", "video_file_id", "title", "description", "caption", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultCachedVoiceFormat = jsonFormat(InlineQueryResultCachedVoice, "id", "voice_file_id", "title", "reply_markup", "input_message_content", "type")
  implicit val inlineQueryResultCachedAudio = jsonFormat(InlineQueryResultCachedAudio, "id", "audio_file_id", "reply_markup", "input_message_content", "type")

  implicit object inlineQueryResultFormat extends JsonFormat[InlineQueryResult] {
    def write(r: InlineQueryResult) = r match {
      case i: InlineQueryResultArticle => i.toJson
      case i: InlineQueryResultPhoto => i.toJson
      case i: InlineQueryResultGif => i.toJson
      case i: InlineQueryResultMpeg4Gif => i.toJson
      case i: InlineQueryResultVideo => i.toJson
      case i: InlineQueryResultAudio => i.toJson
      case i: InlineQueryResultVoice => i.toJson
      case i: InlineQueryResultDocument => i.toJson
      case i: InlineQueryResultLocation => i.toJson
      case i: InlineQueryResultVenue => i.toJson
      case i: InlineQueryResultContact => i.toJson

      case i: InlineQueryResultCachedPhoto => i.toJson
      case i: InlineQueryResultCachedGif => i.toJson
      case i: InlineQueryResultCachedMpeg4Gif => i.toJson
      case i: InlineQueryResultCachedSticker => i.toJson
      case i: InlineQueryResultCachedDocument => i.toJson
      case i: InlineQueryResultCachedVideo => i.toJson
      case i: InlineQueryResultCachedVoice => i.toJson
      case i: InlineQueryResultCachedAudio => i.toJson
    }
    def read(v: JsValue) = InlineQueryResultPhoto("", "", "")
  }
}
