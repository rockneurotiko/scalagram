package io.rockneurotiko.scalagram.types

case class Audio(
  id: String,
  duration: Int,
  performer: Option[String] = None,
  title: Option[String] = None,
  mimeType: Option[String] = None,
  fileSize: Option[Int] = None)
