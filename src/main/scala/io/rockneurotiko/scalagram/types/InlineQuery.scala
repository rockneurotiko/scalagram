package io.rockneurotiko.scalagram.types

case class InlineQuery(
  id: String,
  from: User,
  query: String,
  offset: String,
  location: Option[Location] = None)
