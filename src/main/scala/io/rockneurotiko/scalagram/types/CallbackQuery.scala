package io.rockneurotiko.scalagram.types

case class CallbackQuery(
  id: String,
  from: User,
  message: Option[Message] = None,
  inlineMessageId: Option[String] = None,
  data: Option[String] = None)
