package io.rockneurotiko.scalagram.types

case class User(
  id: Long,
  firstName: String,
  lastName: Option[String] = None,
  username: Option[String] = None)
