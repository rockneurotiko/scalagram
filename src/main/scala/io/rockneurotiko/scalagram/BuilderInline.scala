package io.rockneurotiko.scalagram

import io.rockneurotiko.scalagram.types._
import io.rockneurotiko.scalagram.Implicits._

class InlineText(val elem: InputTextMessageContent) extends Builder[InputTextMessageContent] {
}
object InlineText {
  def apply(text: String) = new InlineText(InputTextMessageContent(text))
}

class InlineArticle(val elem: InlineQueryResultArticle) extends Builder[InlineQueryResultArticle] {

}
object InlineArticle {
  def apply(id: String, title: String, inputMessageContent: InputMessageContent) = new InlineArticle(InlineQueryResultArticle(id, title, inputMessageContent))
  // def apply[T <: InputMessageContent](id: String, title: String, inputMessageContent: Builder[T]) = new InlineArticle(InlineQueryResultArticle(id, title, inputMessageContent.end()))
}

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
