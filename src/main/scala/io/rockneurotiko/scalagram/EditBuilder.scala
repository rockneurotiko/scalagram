package io.rockneurotiko.scalagram

import io.rockneurotiko.scalagram.types._
import io.rockneurotiko.scalagram.Implicits._
import scala.concurrent.Future

class EditBuilder(cid: Option[Either[String, Long]], mid: Option[Long], iid: Option[String], api: Api)  {
  def text(t: String) = new TextEditBuilder(EditMessageText(cid, mid, iid, t), api)
  // def caption(c: String) = new CaptionEditBuilder(cid, mid, iid, c)
  // def markup() = new MarkupEditBuilder(cid, mid, iid)
}
object EditBuilder {
  def apply(id: Either[String, Long], mid: Long, api: Api): EditBuilder = new EditBuilder(id, mid, None, api)
  def apply(id: String, api: Api): EditBuilder = new EditBuilder(None, None, id, api)
}

class TextEditBuilder(val elem: EditMessageText, val api: Api) extends RequestBuilder[EditMessageText, Either[Message, Boolean]] {
  val fac = (elem: EditMessageText) => new TextEditBuilder(elem, api)
  val copyMarkup = (e: EditMessageText, i: InlineKeyboardMarkup) => e.copy(replyMarkup = i)

  def parseMode(m: String) = fac(elem.copy(parseMode = m))
  def disablePreview(d: Boolean = true) = fac(elem.copy(disablePreview = d))

  def keyboard(key: InlineKeyboard) = fac(copyMarkup(elem, key.end()))
  def keyboard(key: InlineKeyboardMarkup) = fac(copyMarkup(elem, key))

  def end() = api.editMessageText(elem)
}
