package io.rockneurotiko.scalagram

import io.rockneurotiko.scalagram.types._
import io.rockneurotiko.scalagram.Implicits._

class Button(val elem: KeyboardButton) extends Builder[KeyboardButton] {
  def text(t: String) = Button(elem.copy(text = t))
  def requestContact(b: Boolean) = Button(elem.copy(requestContact = b))
  def requestLocation(b: Boolean) = Button(elem.copy(requestLocation = b))

  def `:+`(other: Button) = List(this, other)
}
object Button {
  def apply(t: String): Button = Button(KeyboardButton(t))
  def apply(k: KeyboardButton): Button = new Button(k)
}

class Keyboard(val elem: ReplyKeyboardMarkup) extends Builder[ReplyKeyboardMarkup] {
  def keyboard(key: Seq[Seq[KeyboardButton]]) = Keyboard(elem.copy(keyboard = key))
  def resize(b: Boolean = true) = Keyboard(elem.copy(resizeKeyboard = b))
  def oneTime(b: Boolean = true) = Keyboard(elem.copy(oneTimeKeyboard = b))
  def selective(b: Boolean = true) = Keyboard(elem.copy(selective = b))

  def row(r: Seq[KeyboardButton] = List()): Keyboard = Keyboard(elem.copy(keyboard = elem.keyboard :+ r))
  def button(e: KeyboardButton) = Keyboard(elem.copy(keyboard = elem.keyboard.init :+ (elem.keyboard.lastOption.getOrElse(List()) :+ e)))

  // Operators
  // Add button!
  def `>` = this.button _
  // def `+` = this.button _

  // Add button to new row
  def `>>`(e: KeyboardButton): Keyboard = this.row(List(e))
  // Merge keyboard buttons
  def `++`(other: Keyboard) = this.keyboard(elem.keyboard ++ other.end().keyboard)
  // Add row
  def `:+` = this.row _
}
object Keyboard {
  def apply(): Keyboard = Keyboard(ReplyKeyboardMarkup(List()))
  def apply(k: Seq[Seq[Button]]): Keyboard = Keyboard(ReplyKeyboardMarkup(k map {r => r.map {_.end}}))
  def apply(k: ReplyKeyboardMarkup): Keyboard = new Keyboard(k)
}
