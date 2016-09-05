package io.rockneurotiko.scalagram.types

import spray.httpx.SprayJsonSupport
import spray.json._

object TypesJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val userFormat = jsonFormat(User, "id", "first_name", "last_name", "username")

  implicit val audioFormat = jsonFormat(Audio, "file_id", "duration", "performer", "title", "mime_type", "file_size")
  implicit val chatMemberFormat = jsonFormat(ChatMember, "user", "status")
  implicit val chatFormat = jsonFormat(Chat, "id", "title")
  implicit val contactFormat = jsonFormat(Contact, "phone_number", "first_name", "last_name", "user_id")
  implicit val photoSizeFormat = jsonFormat(PhotoSize, "file_id", "width", "height", "file_size")
  implicit val documentFormat = jsonFormat(Document, "file_id", "thumb", "file_name", "mime_type", "file_size")
  implicit val fileFormat = jsonFormat(File, "file_id", "file_size", "file_path")
  implicit val locationFormat = jsonFormat(Location, "longitude", "latitude")
  implicit val stickerFormat = jsonFormat(Sticker, "file_id", "width", "height", "thumb", "emoji", "file_size")
  implicit val userProfilePhotosFormat = jsonFormat(UserProfilePhotos, "total_count", "photos")
  implicit val venueFormat = jsonFormat(Venue, "location", "title", "address", "foursquare_id")
  implicit val videoFormat = jsonFormat(Video, "file_id", "width", "height", "duration", "thumb", "mime_type", "file_size")
  implicit val voiceFormat = jsonFormat(Voice, "file_id", "duration", "mime_type", "file_size")

  implicit val messageEntityFormat = jsonFormat(MessageEntity, "type", "offset", "length", "url", "user")

  // Dirty trick to be able to format json with number of fields > 22
  implicit object MessageFormat extends RootJsonFormat[Message] {
    def write(m: Message) = JsObject()
    def read(value: JsValue) = {
      val vjso = value.asJsObject;
      val fields = vjso.fields;

      val Some(JsNumber(id)) = fields.get("message_id");
      val from = fields.get("from");
      val Some(JsNumber(date)) = fields.get("date");
      val Some(chat) = fields.get("chat");
      val forwardFrom = fields.get("forward_from");
      val forwardFromChat = fields.get("forward_from_chat");
      val forwardDate = fields.get("forward_date");
      val replyTo = fields.get("reply_to_message");
      val editDate = fields.get("edit_date");
      val text = fields.get("text");
      val entities = fields.get("entities");
      val audio = fields.get("audio");
      val document = fields.get("document");
      val photo = fields.get("photo");
      val sticker = fields.get("sticker");
      val video = fields.get("video");
      val voice = fields.get("voice");
      val caption = fields.get("caption");
      val contact = fields.get("contact");
      val location = fields.get("location");
      val venue = fields.get("venue");
      val newChatMember = fields.get("new_chat_member");
      val leftChatMember = fields.get("left_chat_member");
      val newChatTitle = fields.get("new_chat_title");
      val newChatPhoto = fields.get("new_chat_photo");
      val deleteChatPhoto = fields.get("delete_chat_photo");
      val groupChatCreated = fields.get("group_chat_created");
      val channelChatCreated = fields.get("channel_chat_created");
      val migrateToChatId = fields.get("migrate_to_chat_id");
      val migrateFromChatId = fields.get("migrate_from_chat_id");
      val pinnedMessage = fields.get("pinned_message");

      Message(
        id.toLong,
        from.flatMap(_.convertTo[Option[User]]),
        date.toInt,
        chat.convertTo[Either[User, Chat]],
        forwardFrom.flatMap(_.convertTo[Option[User]]),
        forwardFromChat.flatMap(_.convertTo[Option[Chat]]),
        forwardDate.flatMap(_.convertTo[Option[Int]]),
        replyTo.flatMap(_.convertTo[Option[Message]]),
        editDate.flatMap(_.convertTo[Option[Int]]),
        text.flatMap(_.convertTo[Option[String]]),
        entities.flatMap(_.convertTo[Option[List[MessageEntity]]]),
        audio.flatMap(_.convertTo[Option[Audio]]),
        document.flatMap(_.convertTo[Option[Document]]),
        photo.flatMap(_.convertTo[Option[List[PhotoSize]]]),
        sticker.flatMap(_.convertTo[Option[Sticker]]),
        video.flatMap(_.convertTo[Option[Video]]),
        voice.flatMap(_.convertTo[Option[Voice]]),
        caption.flatMap(_.convertTo[Option[String]]),
        contact.flatMap(_.convertTo[Option[Contact]]),
        location.flatMap(_.convertTo[Option[Location]]),
        venue.flatMap(_.convertTo[Option[Venue]]),
        newChatMember.flatMap(_.convertTo[Option[User]]),
        leftChatMember.flatMap(_.convertTo[Option[User]]),
        newChatTitle.flatMap(_.convertTo[Option[String]]),
        newChatPhoto.flatMap(_.convertTo[Option[List[PhotoSize]]]),
        deleteChatPhoto.flatMap(_.convertTo[Option[Boolean]]),
        groupChatCreated.flatMap(_.convertTo[Option[Boolean]]),
        channelChatCreated.flatMap(_.convertTo[Option[Boolean]]),
        migrateToChatId.flatMap(_.convertTo[Option[Long]]),
        migrateFromChatId.flatMap(_.convertTo[Option[Long]]),
        pinnedMessage.flatMap(_.convertTo[Option[Message]]))

      // (vjso.getFields("message_id", "from", "date", "chat", "forward_from", "forward_from_chat", "forward_date", "reply_to_message", "edit_date", "text", "entities", "audio", "document", "photo", "sticker", "video", "voice", "caption", "contact", "location", "venue", "new_chat_member"), vjso.getFields("left_chat_member", "new_chat_title", "new_chat_photo", "delete_chat_photo", "group_chat_created", "channel_chat_created", "migrate_to_chat_id", "migrate_from_chat_id", "pinned_message")) match {
      //   case (Seq(JsNumber(id), from, JsNumber(date), chat, forwardFrom, forwardFromChat, forwardDate, replyTo, editDate, text, entities, audio, document, photo, sticker, video, voice, caption, contact, location, venue, newChatMember), Seq(leftChatMember, newChatTitle, newChatPhoto, deleteChatPhoto, groupChatCreated, channelChatCreated, migrateToChatId, migrateFromChatId, pinnedMessage)) =>
      // Message(
      //       id.toLong,
      //       from.convertTo[Option[User]],
      //       date.toInt,
      //       chat.convertTo[Either[User, Chat]],
      //       forwardFrom.convertTo[Option[User]],
      //       forwardFromChat.convertTo[Option[Chat]],
      //       forwardDate.convertTo[Option[Int]],
      //       replyTo.convertTo[Option[Message]],
      //       editDate.convertTo[Option[Int]],
      //       text.convertTo[Option[String]],
      //       entities.convertTo[Option[List[MessageEntity]]],
      //       audio.convertTo[Option[Audio]],
      //       document.convertTo[Option[Document]],
      //       photo.convertTo[Option[List[PhotoSize]]],
      //       sticker.convertTo[Option[Sticker]],
      //       video.convertTo[Option[Video]],
      //       voice.convertTo[Option[Voice]],
      //       caption.convertTo[Option[String]],
      //       contact.convertTo[Option[Contact]],
      //       location.convertTo[Option[Location]],
      //       venue.convertTo[Option[Venue]],
      //       newChatMember.convertTo[Option[User]],
      //       leftChatMember.convertTo[Option[User]],
      //       newChatTitle.convertTo[Option[String]],
      //       newChatPhoto.convertTo[Option[List[PhotoSize]]],
      //       deleteChatPhoto.convertTo[Option[Boolean]],
      //       groupChatCreated.convertTo[Option[Boolean]],
      //       channelChatCreated.convertTo[Option[Boolean]],
      //       migrateToChatId.convertTo[Option[Long]],
      //       migrateFromChatId.convertTo[Option[Long]],
      //       pinnedMessage.convertTo[Option[Message]])
      //   case e => throw new DeserializationException(s"NotMatch!: \n$e")
      // }
    }
  }

  implicit val callbackQueryFormat = jsonFormat(CallbackQuery, "id", "from", "message", "inline_message_id", "data")
  implicit val chosenInlineResultFormat = jsonFormat(ChosenInlineResult, "result_id", "from", "location", "inline_message_id", "query")

  implicit val inlineQueryFormat = jsonFormat(InlineQuery, "id", "from", "query", "offset", "location")

  // implicit val inlineQueryResultArticleFormat = jsonFormat(InlineQueryResultArticle, "id", "title", "input_message_content", "reply_markup", "url", "hide_url", "description", "thumb_url", "thumb_width", "thumb_height", "type")

  implicit val updateFormat = jsonFormat(Update, "update_id", "message", "edited_message", "inline_query", "chosen_inline_result", "callback_query")
  implicit val failResultFormat = jsonFormat(FailResult, "ok", "error_code", "description")
  implicit def resultFormat[T: JsonFormat] = jsonFormat(Result.apply[T], "ok", "result")

  def extractResult[T](fre: Either[FailResult, Result[T]]): Either[FailResult, T] = fre match {
    case Left(fr) => Left(fr)
    case Right(Result(true, result)) => Right(result)
    case Right(Result(false, _)) => Left(FailResult(false, 9711, "Error decoding"))
  }

  // def stringTransformer[T: JsonFormat]: String => T = t => {println(t); t.parseJson.convertTo[T]}
  def stringTransformer[T: JsonFormat]: String => T = t => t.parseJson.convertTo[T]

  def resultTransformer[T: JsonFormat]: String => Result[T] = stringTransformer[Result[T]]

  implicit def eitherFailTransformer[T: JsonFormat]: String => Either[FailResult, Result[T]] = stringTransformer[Either[FailResult, Result[T]]]

  // def eitherFailExtractorTransformer[T: JsonFormat]: String => Either[FailResult, T] =
  //   t => extractResult(eitherFailTransformer[T].apply(t))

  // implicit def testImplicit[T: JsonFormat]: String => Either[FailResult, T] = t => extractResult(t.parseJson.convertTo[Either[FailResult, Result[T]]])
}
