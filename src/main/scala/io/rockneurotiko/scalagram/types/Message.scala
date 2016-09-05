package io.rockneurotiko.scalagram.types

case class Message(
  id: Long,
  from: Option[User] = None,
  date: Int,
  chat: Either[User, Chat],

  forwardFrom: Option[User] = None,
  forwardFromChat: Option[Chat] = None,
  forwardDate: Option[Int] = None,

  replyTo: Option[Message] = None,

  editDate: Option[Int] = None,

  text: Option[String] = None,
  entities: Option[List[MessageEntity]] = None,
  audio: Option[Audio] = None,
  document: Option[Document] = None,
  photo: Option[List[PhotoSize]] = None,
  sticker: Option[Sticker] = None,
  video: Option[Video] = None,
  voice: Option[Voice] = None,
  caption: Option[String] = None,
  contact: Option[Contact] = None,
  location: Option[Location] = None,
  venue: Option[Venue] = None,

  newChatMember: Option[User] = None,
  leftChatMember: Option[User] = None,
  newChatTitle: Option[String] = None,
  newChatPhoto: Option[List[PhotoSize]] = None,
  deleteChatPhoto: Option[Boolean] = None,
  groupChatCreated: Option[Boolean] = None,
  channelChatCreated: Option[Boolean] = None,
  migrateToChatId: Option[Long] = None,
  migrateFromChatId: Option[Long] = None,
  pinnedMessage: Option[Message] = None
) {
  def sender = chat.fold(u => u.id, c => c.id)
}
