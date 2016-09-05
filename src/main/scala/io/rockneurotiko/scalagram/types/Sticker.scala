package io.rockneurotiko.scalagram.types

case class Sticker(
  id: String,
  width: Int,
  height: Int,
  thumb: Option[PhotoSize] = None,
  emoji: Option[String] = None,
  fileSize: Option[Int])
