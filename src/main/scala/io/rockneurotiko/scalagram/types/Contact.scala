package io.rockneurotiko.scalagram.types

case class Contact(
  phoneNumber: String,
  firstName: String,
  lastName: Option[String] = None,
  userId: Option[Long] = None)
