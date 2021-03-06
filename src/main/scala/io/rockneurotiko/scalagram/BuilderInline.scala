package io.rockneurotiko.scalagram

import io.rockneurotiko.scalagram.types._
import io.rockneurotiko.scalagram.Implicits._

trait InlineMarkuper[T <: ReplyInlineKeyboardT] {
  this: Builder[T] =>
  val fac: T => Builder[T]
  val copyMarkup: (T, InlineKeyboardMarkup) => T

  def keyboard(key: InlineKeyboard) = fac(copyMarkup(elem, key.end()))
  def keyboard(key: InlineKeyboardMarkup) = fac(copyMarkup(elem, key))
}

// Generic Content creator by the parameters
object InlineContent {
  def apply(text: String) = new InlineTextContent(InputTextMessageContent(text))
  def apply(phone: String, first: String) = new InlineContactContent(InputContactMessageContent(phone, first))
  def apply(latitude: Double, longitude: Double) = new InlineLocationContent(InputLocationMessageContent(latitude, longitude))
  def apply(latitude: Double, longitude: Double, title: String, address: String) = new InlineVenueContent(InputVenueMessageContent(latitude, longitude, title, address))
}

// Message Content
class InlineTextContent(val elem: InputTextMessageContent) extends Builder[InputTextMessageContent] {
  def fac(t: InputTextMessageContent) = InlineTextContent(t)
  def text(t: String) = fac(elem.copy(messageText = t))
  def parseMode(parse: String) = fac(elem.copy(parseMode = parse))
  def disablePreview(d: Boolean = true) = fac(elem.copy(disableWebPagePreview = d))
}
object InlineTextContent {
  def apply(text: String) = new InlineTextContent(InputTextMessageContent(text))
  def apply(t: InputTextMessageContent) = new InlineTextContent(t)
}

class InlineLocationContent(val elem: InputLocationMessageContent) extends Builder[InputLocationMessageContent] {
  def latitude(l: Double) = InlineLocationContent(l, elem.longitude)
  def longitude(l: Double) = InlineLocationContent(elem.latitude, l)
}
object InlineLocationContent {
  def apply(latitude: Double, longitude: Double) = new InlineLocationContent(InputLocationMessageContent(latitude, longitude))
}

class InlineVenueContent(val elem: InputVenueMessageContent) extends Builder[InputVenueMessageContent] {
  def fac(i: InputVenueMessageContent) = InlineVenueContent(i)
  def latitude(l: Double) = fac(elem.copy(latitude  = l))
  def longitude(l: Double) = fac(elem.copy(longitude = l))
  def title(t: String) = fac(elem.copy(title = t))
  def address(a: String) = fac(elem.copy(address = a))
  def foursquare(i: String) = fac(elem.copy(foursquareId = i))
}
object InlineVenueContent {
  def apply(latitude: Double, longitude: Double, title: String, address: String) = new InlineVenueContent(InputVenueMessageContent(latitude, longitude, title, address))
  def apply(i: InputVenueMessageContent) = new InlineVenueContent(i)
}

class InlineContactContent(val elem: InputContactMessageContent) extends Builder[InputContactMessageContent] {
  def phone(p: String) = new InlineContactContent(elem.copy(phoneNumber = p))
  def firstName(n: String) = new InlineContactContent(elem.copy(firstName = n))
  def lastName(n: String) = new InlineContactContent(elem.copy(lastName = n))
}
object InlineContactContent {
  def apply(phone: String, first: String) = new InlineContactContent(InputContactMessageContent(phone, first))
}

// Inline Query Result
class InlineArticle(val elem: InlineQueryResultArticle) extends Builder[InlineQueryResultArticle] with InlineMarkuper[InlineQueryResultArticle] {
  val fac = (i: InlineQueryResultArticle) => new InlineArticle(i)
  val copyMarkup = (e: InlineQueryResultArticle, i: InlineKeyboardMarkup) => e.copy(replyMarkup = i)

  def id(e: String) = fac(elem.copy(id = e))
  def title(e: String) = fac(elem.copy(title = e))
  def inputMessageContent(e: InputMessageContent) = fac(elem.copy(inputMessageContent = e))
  // Options
  def url(e: String) = fac(elem.copy(url = e))
  def hideUrl(e: Boolean = true) = fac(elem.copy(hideUrl = e))
  def description(e: String) = fac(elem.copy(description = e))
  def thumbUrl(e: String) = fac(elem.copy(thumbUrl = e))
  def thumbWidth(e: Int) = fac(elem.copy(thumbWidth = e))
  def thumbHeight(e: Int) = fac(elem.copy(thumbHeight = e))
}
object InlineArticle {
  def apply(id: String, title: String, inputMessageContent: InputMessageContent) = new InlineArticle(InlineQueryResultArticle(id, title, inputMessageContent))
  def apply(elem: InlineQueryResultArticle) = new InlineArticle(elem)
}

class InlinePhoto(val elem: InlineQueryResultPhoto) extends Builder[InlineQueryResultPhoto] with InlineMarkuper[InlineQueryResultPhoto] {
  val fac = (i: InlineQueryResultPhoto) => new InlinePhoto(i)
  val copyMarkup = (e: InlineQueryResultPhoto, i: InlineKeyboardMarkup) => e.copy(replyMarkup = i)

  def id(e: String) = fac(elem.copy(id = e))
  def photoUrl(e: String) = fac(elem.copy(photoUrl = e))
  def thumbUrl(e: String) = fac(elem.copy(thumbUrl = e))
  // Options
  def photoWidth(e: Int) = fac(elem.copy(photoWidth = e))
  def photoHeight(e: Int) = fac(elem.copy(photoHeight = e))
  def title(e: String) = fac(elem.copy(title = e))
  def description(e: String) = fac(elem.copy(description = e))
  def caption(e: String) = fac(elem.copy(caption = e))
  def inputMessageContent(e: InputMessageContent) = fac(elem.copy(inputMessageContent = e))
}
object InlinePhoto {
  def apply(id: String, url: String, thumb: String) = new InlinePhoto(InlineQueryResultPhoto(id, url, thumb))
}
class InlineGif(val elem: InlineQueryResultGif) extends Builder[InlineQueryResultGif] {}
class InlineMpeg(val elem: InlineQueryResultMpeg4Gif) extends Builder[InlineQueryResultMpeg4Gif] {}
class InlineVideo(val elem: InlineQueryResultVideo) extends Builder[InlineQueryResultVideo] {}
class InlineAudio(val elem: InlineQueryResultAudio) extends Builder[InlineQueryResultAudio] {}
class InlineVoice(val elem: InlineQueryResultVoice) extends Builder[InlineQueryResultVoice] {}
class InlineDocument(val elem: InlineQueryResultDocument) extends Builder[InlineQueryResultDocument] {}
class InlineLocation(val elem: InlineQueryResultLocation) extends Builder[InlineQueryResultLocation] {}
class InlineVenue(val elem: InlineQueryResultVenue) extends Builder[InlineQueryResultVenue] {}
class InlineContact(val elem: InlineQueryResultContact) extends Builder[InlineQueryResultContact] {}

class InlineCachePhoto(val elem: InlineQueryResultCachedPhoto) extends Builder[InlineQueryResultCachedPhoto] {}
class InlineCacheGif(val elem: InlineQueryResultCachedGif) extends Builder[InlineQueryResultCachedGif] {}
class InlineCacheMpeg(val elem: InlineQueryResultCachedMpeg4Gif) extends Builder[InlineQueryResultCachedMpeg4Gif] {}
class InlineCacheSticker(val elem: InlineQueryResultCachedSticker) extends Builder[InlineQueryResultCachedSticker] {}
class InlineCacheDocument(val elem: InlineQueryResultCachedDocument) extends Builder[InlineQueryResultCachedDocument] {}
class InlineCacheVideo(val elem: InlineQueryResultCachedVideo) extends Builder[InlineQueryResultCachedVideo] {}
class InlineCacheVoice(val elem: InlineQueryResultCachedVoice) extends Builder[InlineQueryResultCachedVoice] {}
class InlineCacheAudio(val elem: InlineQueryResultCachedAudio) extends Builder[InlineQueryResultCachedAudio] {}


// Inline Keyboards!

class InlineButton(val elem: InlineKeyboardButton) extends Builder[InlineKeyboardButton] {
  def text(t: String) = InlineButton(elem.copy(text = t))
  def url(u: String) = InlineButton(elem.copy(url = u))
  def data(d: String) = InlineButton(elem.copy(callbackData = d))
  def switch(s: String) = InlineButton(elem.copy(switchInlineQuery = s))

  def `:+`(other: InlineButton) = List(this, other)
}
object InlineButton {
  def apply(t: String): InlineButton = InlineButton(InlineKeyboardButton(t))
  def apply(k: InlineKeyboardButton): InlineButton = new InlineButton(k)
}

class InlineKeyboard(val elem: InlineKeyboardMarkup) extends Builder[InlineKeyboardMarkup] {
  def keyboard(key: Seq[Seq[InlineKeyboardButton]]) = InlineKeyboard(elem.copy(inlineKeyboard = key))

  def row(r: Seq[InlineKeyboardButton] = List()): InlineKeyboard = InlineKeyboard(elem.copy(inlineKeyboard = elem.inlineKeyboard :+ r))
  def button(e: InlineKeyboardButton) = InlineKeyboard(elem.copy(inlineKeyboard = elem.inlineKeyboard.init :+ (elem.inlineKeyboard.lastOption.getOrElse(List()) :+ e)))

  // Operators
  // Add button!
  def `>` = this.button _
  // def `+` = this.button _

  // Add button to new row
  def `>>`(e: InlineKeyboardButton): InlineKeyboard = this.row(List(e))
  // Merge keyboard buttons
  def `++`(other: InlineKeyboard) = this.keyboard(elem.inlineKeyboard ++ other.end().inlineKeyboard)
  // Add row
  def `:+` = this.row _
}
object InlineKeyboard {
  def apply(): InlineKeyboard = InlineKeyboard(InlineKeyboardMarkup(List()))
  def apply(k: Seq[Seq[InlineButton]]): InlineKeyboard = InlineKeyboard(InlineKeyboardMarkup(k map {r => r.map {_.end}}))
  def apply(k: InlineKeyboardMarkup): InlineKeyboard = new InlineKeyboard(k)
}
