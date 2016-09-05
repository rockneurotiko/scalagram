package io.rockneurotiko.scalagrammacros

import scala.language.experimental.macros

object Macros {
  import scala.reflect.macros._
  import blackbox.Context

  def hello(): Unit = macro hello_impl

  def hello_impl(c: Context)(): c.Expr[Unit] = {
    import c.universe._
    reify { println("Hello World!") }
  }
}
