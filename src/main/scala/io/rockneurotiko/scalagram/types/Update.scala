package io.rockneurotiko.scalagram.types

case class Update(
  id: Long,
  message: Option[Message] = None,
  editedMessage: Option[Message] = None,
  inlineQuery: Option[InlineQuery] = None,
  chosenInlineResult: Option[ChosenInlineResult] = None,
  callbackQuery: Option[CallbackQuery] = None)
