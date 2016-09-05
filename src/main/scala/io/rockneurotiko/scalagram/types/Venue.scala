package io.rockneurotiko.scalagram.types

case class Venue(
  location: Location,
  title: String,
  address: String,
  foursquareId: Option[String] = None)
