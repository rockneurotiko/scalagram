package io.rockneurotiko.scalagram.types

case class Result[T](status: Boolean, result: T)
