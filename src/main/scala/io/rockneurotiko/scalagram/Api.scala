package io.rockneurotiko.scalagram

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

import Implicits._
import io.rockneurotiko.scalagram.types._
import io.rockneurotiko.scalagram.types.ReplyMarkupSupport._
import io.rockneurotiko.scalagram.types.RequestsJsonSupport._
import io.rockneurotiko.scalagram.types.TypesJsonSupport._
import spray.http._
import spray.json._

class Api (val token: String, requestManager: ApiRequestManager = DefaultApiRequestManager(), DEBUG: Boolean = true) extends DefaultJsonProtocol {
  private val apiUrl = s"https://api.telegram.org/bot$token/"
  private val fileUrl = s"https://api.telegram.org/file/bot$token/"

  private val subscriptions: ListBuffer[ApiSubscription] = ListBuffer()

  def url(p: String) = s"$apiUrl$p"

  def addSubscription(s: ApiSubscription): ApiSubscription = {subscriptions += s; s}
  def removeSubscription(uuid: String): Any = subscriptions.find(_.uuid == uuid).foreach(subscriptions -= _)
  def removeSubscription(api: ApiSubscription): Any = removeSubscription(api.uuid)
  // Middlewares?

  private def applySubscriptions(updates: List[Update]) = updates.foreach(u => {
    if (DEBUG) { println(u) }
    subscriptions.foreach(s => {
      if (s.condition(u)) Try(s.handle(u))
    })})

  def handler(f: Either[FailResult, List[Update]]): Any = defaultHandler(f)
  def syncFutureHandler(f: Either[FailResult, List[Update]]): Future[Any] = Future {
    defaultHandler(f)
  }
  def asyncHandler(f: Future[Either[FailResult, List[Update]]]): Future[Any] = f.map(l => defaultHandler(l))

  def answer(implicit m: Message): MessageBuilder = MessageBuilder(m, this)
  def send(i: Long): MessageBuilder = MessageBuilder(i, this)
  def send(u: String): MessageBuilder = MessageBuilder(u, this)

  // BLOCKING!
  def longPoll(offset: Int = 0, limit: Int = 100, timeout: Int = 30): Unit = {
    import scala.concurrent._
    import scala.concurrent.duration._
    import scala.util.Try
    import ExecutionContext.Implicits.global

    @scala.annotation.tailrec
    def inner(offset: Int, limit: Int, timeout: Int): Unit = {
      val gu = GetUpdates(offset, limit, timeout)
      // val updates = Try(Await.result(getUpdates(gu), timeout + 10 seconds))
      val updates = Await.result(getUpdates(gu), timeout + 10 seconds)
      val newoffset = getOffset(updates, offset)
      Try(handler(updates))
      inner(newoffset, limit, timeout)
    }
    Await.result(setWebhook(), 5 seconds)
    inner(offset, limit, timeout)
  }

  def getOffset(ups: Either[FailResult, List[Update]], off: Int): Int =
    ups.fold(
      _ => off,
      l => l.foldLeft(off) {(old, upd) => if (upd.id >= old) upd.id.toInt + 1 else old}
    )

  private def defaultHandler(eitherupdates: Either[FailResult, List[Update]]) = eitherupdates match {
    case Left(fail) => if (DEBUG) println(fail)
    case Right(updates) => applySubscriptions(updates)
  }

  private def buildFileBodyPart(key: String, file: InputFile) = {
    val httpData = HttpData(file.bytes)
    val httpEntitiy = HttpEntity(MediaTypes.`multipart/form-data`, httpData).asInstanceOf[HttpEntity.NonEmpty]
    BodyPart(FormFile(file.name, httpEntitiy), key)
  }

  private def buildParameterBodyPart(key: String, value: String) = {
    BodyPart(value, Seq(HttpHeaders.`Content-Disposition`("form-data", Map("name" -> key)) ))
  }

  private def buildMultiPart(file: InputFile, nameFile: String, extraSeq: Seq[Option[(String, String)]]): MultipartFormData = {
    val fileBodyPart = buildFileBodyPart(nameFile, file)
    val extra = extraSeq.flatten.map { x => buildParameterBodyPart(x._1, x._2)}
    MultipartFormData(Seq(fileBodyPart) ++ extra)
  }

  private def middleware[T](f: Future[Either[FailResult, Result[T]]]): Future[Either[FailResult, T]] = f map { extractResult(_) }

  private def genericPost[T: JsonFormat](part: String, m: Option[RequestMessage] = None): Future[Either[FailResult, T]] =
    middleware[T](this.requestManager.post(url(part), m map {_.toJson.asJsObject}))

  private def postReceiveMessage(part: String, m: Option[RequestMessage] = None): Future[Either[FailResult, Message]] = genericPost[Message](part, m)

  private def genericMultipart[T: JsonFormat](part: String, m: MultipartFormData): Future[Either[FailResult, T]] =
    middleware[T](this.requestManager.multipart(url(part), m))

  // def handle(r: RequestMessage) = r match {
  //   case s: SendText => sendMessage(s)
  //   case f: ForwardMessage => forwardMessage(f)
  //   case f: SendPhoto => sendPhoto(f)
  //   case a: SendAudio => sendAudio(a)
  //   case v: SendVoice => sendVoice(v)
  //   case s: SendDocument => sendDocument(s)
  //   case s: SendSticker => sendSticker(s)
  //   case c: SendChatAction => sendChatAction(c)
  //   case l: SendLocation => sendLocation(l)
  //   case v: SendVideo => sendVideo(v)
  //   case v: SendVenue => sendVenue(v)
  //   case l: SendContact => sendContact(l)
  //   case u: GetUserProfilePhotos => getUserProfilePhotos(u)
  // }

  // All this methods returns Future[Either[FailResult, T]]

  def getMe() = genericPost[User]("getMe")

  def getUpdates(updates: Option[GetUpdates] = None) = genericPost[List[Update]]("getUpdates", updates)

  def setWebhook(webhook: SetWebhook = SetWebhook("")) = webhook.certificate match {
    case Some(inputFile) => {
      val extra = Seq(webhook.url.map((_, "url")))
      val multipart = buildMultiPart(inputFile, "certificate", extra)
      genericMultipart[Boolean]("setWebhook", multipart)
    }
    case None => genericPost[Boolean]("setWebhook", webhook)
  }

  def sendMessage(m: SendText) = postReceiveMessage("sendMessage", m)

  def forwardMessage(m: ForwardMessage) = postReceiveMessage("forwardMessage", m)

  def sendPhoto(f: SendPhoto): Future[Either[FailResult, Message]] = f.photo match {
    case Left(inputFile) => {
      val extra = Seq(
        Some(f.chatId.fold((_, "chat_id"), y => (y.toString, "chat_id"))),
        f.caption.map((_, "caption")),
        f.disableNotification.map(d => (d.toString, "disable_notification")),
        f.replyTo.map {r => (r.toString, "reply_to_message_id")},
        f.replyMarkup.map {r => (r.toJson.toString, "reply_markup")}
      )
      val multipart = buildMultiPart(inputFile, "photo", extra)
      genericMultipart[Message]("sendPhoto", multipart)
    }
    case Right(_) => {
      postReceiveMessage("sendPhoto", f)
    }
  }

  def sendAudio(a: SendAudio) = a.audio match {
    case Left(inputFile) => {
      val extra = Seq(
        Some(a.chatId.fold((_, "chat_id"), y => (y.toString, "chat_id"))),
        a.duration.map(d => (d.toString, "duration")),
        a.performer.map((_, "performer")),
        a.title.map((_, "title")),
        a.disableNotification.map(d => (d.toString, "disable_notification")),
        a.replyTo.map(r => (r.toString, "reply_to_message_id")),
        a.replyMarkup.map(r => (r.toJson.toString, "reply_markup"))
      )
      val multipart = buildMultiPart(inputFile, "audio", extra)
      genericMultipart[Message]("sendAudio", multipart)
    }
    case Right(_) => {
      postReceiveMessage("sendAudio", a)
    }
  }

  def sendDocument(a: SendDocument): Future[Either[FailResult, Message]] = a.document match {
    case Left(inputFile) => {
      val extra = Seq(
        Some(a.chatId.fold((_, "chat_id"), y => (y.toString, "chat_id"))),
        a.caption.map((_, "caption")),
        a.disableNotification.map(d => (d.toString, "disable_notification")),
        a.replyTo.map(r => (r.toString, "reply_to_message_id")),
        a.replyMarkup.map(r => (r.toJson.toString, "reply_markup"))
      )
      val multipart = buildMultiPart(inputFile, "document", extra)
      genericMultipart[Message]("sendDocument", multipart)
    }
    case Right(_) => {
      postReceiveMessage("sendDocument", a)
    }
  }

  def sendSticker(a: SendSticker): Future[Either[FailResult, Message]] = a.sticker match {
    case Left(inputFile) => {
      val extra = Seq(
        Some(a.chatId.fold((_, "chat_id"), y => (y.toString, "chat_id"))),
        a.disableNotification.map(d => (d.toString, "disable_notification")),
        a.replyTo.map(r => (r.toString, "reply_to_message_id")),
        a.replyMarkup.map(r => (r.toJson.toString, "reply_markup"))
      )
      val multipart = buildMultiPart(inputFile, "sticker", extra)
      genericMultipart[Message]("sendSticker", multipart)
    }
    case Right(_) => {
      postReceiveMessage("sendSticker", a)
    }
  }

  def sendVideo(a: SendVideo): Future[Either[FailResult, Message]] = a.video match {
    case Left(inputFile) => {
      val extra = Seq(
        Some(a.chatId.fold((_, "chat_id"), y => (y.toString, "chat_id"))),
        a.duration.map(d => (d.toString, "duration")),
        a.width.map(w => (w.toString, "width")),
        a.height.map(h => (h.toString, "height")),
        a.caption.map((_, "caption")),
        a.disableNotification.map(d => (d.toString, "disable_notification")),
        a.replyTo.map(r => (r.toString, "reply_to_message_id")),
        a.replyMarkup.map(r => (r.toJson.toString, "reply_markup"))
      )
      val multipart = buildMultiPart(inputFile, "video", extra)
      genericMultipart[Message]("sendVideo", multipart)
    }
    case Right(_) => {
      postReceiveMessage("sendVideo", a)
    }
  }

  def sendVoice(a: SendVoice): Future[Either[FailResult, Message]] = a.voice match {
    case Left(inputFile) => {
      val extra = Seq(
        Some(a.chatId.fold((_, "chat_id"), y => (y.toString, "chat_id"))),
        a.duration.map(d => (d.toString, "duration")),
        a.disableNotification.map(d => (d.toString, "disable_notification")),
        a.replyTo.map(r => (r.toString, "reply_to_message_id")),
        a.replyMarkup.map(r => (r.toJson.toString, "reply_markup"))
      )
      val multipart = buildMultiPart(inputFile, "voice", extra)
      genericMultipart[Message]("sendVoice", multipart)
    }
    case Right(_) => {
      postReceiveMessage("sendVoice", a)
    }
  }

  def sendLocation(sl: SendLocation) = postReceiveMessage("sendLocation", sl)

  def sendVenue(s: SendVenue) = postReceiveMessage("sendVenue", s)

  def sendContact(s: SendContact) = postReceiveMessage("sendContact", s)

  def sendChatAction(ca: SendChatAction) = postReceiveMessage("sendChatAction", ca)

  def getUserProfilePhotos(c: GetUserProfilePhotos) = genericPost[UserProfilePhotos]("getUserProfilePhotos", c)

  def getFile(g: GetFile) = genericPost[File]("getFile", g)

  def kickChatMember(g: KickChatMember) = genericPost[Boolean]("kickChatMember", g)

  def leaveChat(l: LeaveChat) = genericPost[Boolean]("leaveChat", l)

  def unbanChatMember(u: UnbanChatMember) = genericPost[Boolean]("unbanChatMember", u)

  def getChat(l: GetChat) = genericPost[Chat]("getChat", l)

  def getChatAdministrators(l: GetChatAdministrators) = genericPost[List[ChatMember]]("getChatAdministrators", l)

  def getChatMembersCount(l: GetChatMembersCount) = genericPost[Int]("getChatMembersCount", l)

  def getChatMember(u: GetChatMember) = genericPost[ChatMember]("getChatMember", u)

  def answerCallbackQuery(a: AnswerCallbackQuery) = genericPost[Boolean]("answerCallbackQuery", a)

  def editMessageText(e: EditMessageText) = genericPost[Either[Message, Boolean]]("editMessageText", e)

  def editMessageCaption(e: EditMessageCaption) = genericPost[Either[Message, Boolean]]("editMessageCaption", e)

  def editMessageReplyMarkup(e: EditMessageReplyMarkup) = genericPost[Either[Message, Boolean]]("editMessageReplyMarkup", e)

  def answerInlineQuery(e: AnswerInlineQuery) = genericPost[Boolean]("answerInlineQuery", e)
}

object Api {
  import scala.io.Source

  def tokenFromFile(path: String) = Source.fromFile(path).getLines.toList match {
    case x :: xs => x
    case _ => throw new Exception("No token!")
  }
  def tokenFromEnv(env: String) = sys.env(env)

  def apply(token: String): Api = new Api(token)
  def apply(token: String, debug: Boolean): Api = new Api(token, DEBUG=debug)
  def apply(token: String, requestManager: ApiRequestManager): Api= new Api(token, requestManager)
  def apply(token: String, requestManager: ApiRequestManager, debug: Boolean): Api= new Api(token, requestManager, debug)

  def fromFile(path: String): Api = Api(tokenFromFile(path))
  def fromFile(path: String, requestManager: ApiRequestManager): Api = Api(tokenFromFile(path), requestManager)

  def fromEnv(env: String): Api = Api(tokenFromEnv(env))
  def fromEnv(env: String, requestManager: ApiRequestManager): Api = Api(tokenFromEnv(env), requestManager)
}
