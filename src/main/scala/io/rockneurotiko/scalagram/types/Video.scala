package io.rockneurotiko.scalagram.types

case class Video(
  id: String,
  width: Int,
  height: Int,
  duration: Int,
  thumb: Option[PhotoSize] = None,
  mimeType: Option[String] = None,
  fileSize: Option[Int] = None)
