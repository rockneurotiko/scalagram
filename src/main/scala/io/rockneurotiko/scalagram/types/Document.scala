package io.rockneurotiko.scalagram.types

case class Document(
  id: String,
  thumb: Option[PhotoSize] = None,
  fileName: Option[String] = None,
  mimeType: Option[String] = None,
  fileSize: Option[Int] = None)
