package io.rockneurotiko.scalagram

import io.rockneurotiko.scalagram.types._
import io.rockneurotiko.scalagram.Implicits._
import scala.concurrent.Future

class MessageBuilder(id: Either[String, Long], api: Api) {
  def text(t: String) = TextBuilder(id, t, api)

  def forward(m: Message): ForwardBuilder = ForwardBuilder(id, m, api)
  def forward(from: Either[String, Long], m: Long) = ForwardBuilder(id, from, m, api)

  def photo(f: Either[InputFile, String]) = PhotoBuilder(id, f, api)
  def photoFromPath(p: String) = PhotoBuilder(id, "", api).path(p)
}
object MessageBuilder {
  def apply(m: Message, api: Api): MessageBuilder = MessageBuilder(m.sender, api)
  def apply(id: Either[String, Long], api: Api): MessageBuilder = new MessageBuilder(id, api)
}

// class EndRequest[T](val elem: T) {
//   def get(): T = this.elem
// }

trait RequestBuilder[T, U] {
  val elem: T
  val api: Api
  override def toString() = elem.toString()
  def end(): Future[Either[FailResult, U]]
}

trait Builder[T] {
  val elem: T
  override def toString() = elem.toString()
  def end(): T = this.elem
}

trait Replyer[T <: ReplyToT, U] {
  this: RequestBuilder[T, U] =>
  val fac: T => RequestBuilder[T, U]
  val copyReply: (T, Long) => T
  def replyToId(i: Long) = fac(copyReply(elem, i))
  def reply(m: Message) = replyToId(m.id)
}

trait Markuper[T <: ReplyMarkupT, U] {
  this: RequestBuilder[T, U] =>
  val fac: T => RequestBuilder[T, U]
  val copyMarkup: (T, ReplyMarkup) => T

  def hideKeyboard(selective: Boolean = false, hide: Boolean = true) = fac(copyMarkup(elem, ReplyKeyboardHide(hide, selective)))

  def force(selective: Boolean = false, force: Boolean = true) = fac(copyMarkup(elem, ForceReply(force, selective)))

  def keyboard(key: Keyboard) = fac(copyMarkup(elem, key.end()))
  def keyboard(key: ReplyKeyboardMarkup) = fac(copyMarkup(elem, key))
  def keyboard(key: InlineKeyboardMarkup) = fac(copyMarkup(elem, key))
}

class TextBuilder(val elem: SendText, val api: Api) extends RequestBuilder[SendText, Message] with Replyer[SendText, Message] with Markuper[SendText, Message] {
  val fac = (elem: SendText) => TextBuilder(elem, api)
  val copyReply = (elem: SendText, i: Long) => elem.copy(replyTo = i)
  val copyMarkup = (elem: SendText, m: ReplyMarkup) => elem.copy(replyMarkup = m)

  def text(t: String) = fac(elem.copy(text = t))
  def parseMode(m: String) = fac(elem.copy(parseMode = m))
  def disablePreview(d: Boolean = true) = fac(elem.copy(disablePreview = d))
  def disableNotification(d: Boolean = true) = fac(elem.copy(disableNotification = d))

  def end() = api.sendMessage(elem)
}
object TextBuilder {
  def apply(id: Either[String, Long], text: String, api: Api): TextBuilder = TextBuilder(SendText(id, text), api)
  def apply(send: SendText, api: Api): TextBuilder = new TextBuilder(send, api)
}

class ForwardBuilder(val elem: ForwardMessage, val api: Api) extends RequestBuilder[ForwardMessage, Message] {
  def disableNotification(d: Boolean = true) = ForwardBuilder(elem.copy(disableNotification = d), api)
  def end() = api.forwardMessage(elem)
}
object ForwardBuilder {
  def apply(id: Either[String, Long], m: Message, api: Api): ForwardBuilder = ForwardBuilder(ForwardMessage(id, m.sender, m.id), api)
  def apply(id: Either[String, Long], from: Either[String, Long], m: Long, api: Api): ForwardBuilder = ForwardBuilder(ForwardMessage(id, from, m), api)
  def apply(forw: ForwardMessage, api: Api): ForwardBuilder = new ForwardBuilder(forw, api)
}

class PhotoBuilder(val elem: SendPhoto, val api: Api) extends RequestBuilder[SendPhoto, Message] with Replyer[SendPhoto, Message] with Markuper[SendPhoto, Message] {
  val fac = (newelem: SendPhoto) => PhotoBuilder(newelem, api)
  val copyReply = (elem: SendPhoto, i: Long) => elem.copy(replyTo = i)
  val copyMarkup = (elem: SendPhoto, m: ReplyMarkup) => elem.copy(replyMarkup = m)

  def id(s: String) = fac(elem.copy(photo = s))
  def path(s: String) = fac(elem.copy(photo = InputFile(s)))
  def file(f: InputFile) = fac(elem.copy(photo = f))

  def caption(c: String) = fac(elem.copy(caption = c))
  def disableNotification(d: Boolean = true) = fac(elem.copy(disableNotification = d))

  def end() = api.sendPhoto(elem)
}
object PhotoBuilder {
  def apply(id: Either[String, Long], photo: Either[InputFile, String], api: Api): PhotoBuilder = PhotoBuilder(SendPhoto(id, photo), api)
  def apply(send: SendPhoto, api: Api): PhotoBuilder = new PhotoBuilder(send, api)
}

class AudioBuilder(val elem: SendAudio, val api: Api) extends RequestBuilder[SendAudio, Message] with Replyer[SendAudio, Message] with Markuper[SendAudio, Message] {
  val fac = (newelem: SendAudio) => AudioBuilder(newelem, api)
  val copyReply = (elem: SendAudio, i: Long) => elem.copy(replyTo = i)
  val copyMarkup = (elem: SendAudio, m: ReplyMarkup) => elem.copy(replyMarkup = m)

  def id(s: String) = fac(elem.copy(audio = s))
  def path(s: String) = fac(elem.copy(audio = InputFile(s)))
  def file(f: InputFile) = fac(elem.copy(audio = f))

  def disableNotification(d: Boolean = true) = fac(elem.copy(disableNotification = d))

  def end() = api.sendAudio(elem)
}
object AudioBuilder {
  def apply(id: Either[String, Long], audio: Either[InputFile, String], api: Api): AudioBuilder = AudioBuilder(SendAudio(id, audio), api)
  def apply(send: SendAudio, api: Api): AudioBuilder = new AudioBuilder(send, api)
}
