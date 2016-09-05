package io.rockneurotiko.scalagram.types

case class File(
  id: String,
  size: Option[Int] = None,
  path: Option[String] = None)
