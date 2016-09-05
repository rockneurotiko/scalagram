package io.rockneurotiko.scalagram.types

case class PhotoSize(
  id: String,
  width: Int,
  height: Int,
  fileSize: Option[Int] = None)
