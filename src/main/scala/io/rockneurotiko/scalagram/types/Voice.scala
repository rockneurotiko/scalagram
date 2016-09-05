package io.rockneurotiko.scalagram.types

case class Voice(
  id: String,
  duration: Int,
  mimeType: Option[String] = None,
  fileSize: Option[Int] = None)
