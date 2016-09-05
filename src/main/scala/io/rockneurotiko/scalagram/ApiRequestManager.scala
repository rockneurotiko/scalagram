package io.rockneurotiko.scalagram

import scala.concurrent.Future
import spray.http.MultipartFormData
import spray.json.JsObject

trait ApiRequestManager {
  /** Define two generic methods, post and multipart, that will handle the requests.
    Post receives the data in a spray.json.JsObject, and a method to transform a String to T (String = body received)
    Multipart does the same but instead of JsObject, with MultipartFormData
   */
  def post[T](url: String, data: Option[JsObject] = None) (implicit f: String => T): Future[T]
  def multipart[T](url: String, multi: MultipartFormData) (implicit f: String => T): Future[T]
}
