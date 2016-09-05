package io.rockneurotiko.scalagram.types

import spray.httpx.SprayJsonSupport
import spray.json._

sealed trait InputMessageContent

case class InputTextMessageContent(
  messageText: String,
  parseMode: Option[String] = None,
  disableWebPagePreview: Option[Boolean] = None
) extends InputMessageContent

case class InputLocationMessageContent(
  latitude: Double,
  longitude: Double
) extends InputMessageContent

case class InputVenueMessageContent(
  latitude: Double,
  longitude: Double,
  title : String,
  address: String,
  foursquareId: Option[String] = None
) extends InputMessageContent

case class InputContactMessageContent(
  phoneNumber: String,
  firstName: String,
  lastName: Option[String] = None
) extends InputMessageContent


object InputMessageContentSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val inputTextMessageContentFormat = jsonFormat(InputTextMessageContent, "message_text", "parse_mode", "disable_web_page_preview")
  implicit val inputLocationMessageContentFormat = jsonFormat(InputLocationMessageContent, "latitude", "longitude")
  implicit val inputVenueMessageContentFormat = jsonFormat(InputVenueMessageContent, "latitude", "longitude", "title", "address", "foursquareId")
  implicit val inputContactMessageContentFormat = jsonFormat(InputContactMessageContent, "phoneNumber", "firstName", "lastName")

  implicit object inputMessageContentFormat extends RootJsonFormat[InputMessageContent] {
    def write(im: InputMessageContent) = im match {
      case m: InputTextMessageContent => m.toJson
      case r: InputLocationMessageContent => r.toJson
      case v: InputVenueMessageContent => v.toJson
      case c: InputContactMessageContent => c.toJson
    }
    def read(v: JsValue) = InputTextMessageContent("")
  }
}
