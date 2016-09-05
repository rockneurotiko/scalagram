package io.rockneurotiko.scalagram.types

case class ChosenInlineResult(
  id: String,
  from: User,
  location: Option[Location] = None,
  inlineMessageId: Option[Long] = None,
  query: String)
