package io.rockneurotiko.scalagram

import akka.util.Timeout
import akka.actor.ActorSystem
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import spray.client.pipelining._
import spray.httpx.marshalling._
import spray.httpx.unmarshalling._
import spray.http._
import spray.json._
import spray.httpx.SprayJsonSupport._
import DefaultJsonProtocol._

class DefaultApiRequestManager(implicit val system: ActorSystem = ActorSystem("default-api-request-manager")) extends ApiRequestManager {
  implicit val requestTimeout = Timeout(60 seconds)

  private def decodeWith[T](f: String => T): HttpResponse => T = { response =>
    f(response.entity.asString)
  }

  def post[T](url: String, data: Option[JsObject] = None) (implicit f: String => T): Future[T] = {
    val pipeline = sendReceive ~> decodeWith(f)
    val post = if (data.isDefined) Post(url, data.get) else Post(url)
    pipeline (post)
  }

  // Can call post with MultipartFormData?
  def multipart[T](url: String, multi: MultipartFormData) (implicit f: String => T): Future[T] = {
    val pipeline = sendReceive ~> decodeWith(f)
    pipeline (Post(url, multi))
  }

  // def post[U, T](url: String, data: U) (implicit fd: Marshaller[U],  f: FromResponseUnmarshaller[T]): Future[T] = {
  //   val pipeline = sendReceive ~> unmarshal[T]
  //   pipeline (Post(url, data))
  // }
}

object DefaultApiRequestManager {
  def apply(): DefaultApiRequestManager = new DefaultApiRequestManager()
  def apply(implicit system: ActorSystem): DefaultApiRequestManager = new DefaultApiRequestManager()(system)
}
