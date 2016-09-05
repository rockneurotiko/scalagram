package io.rockneurotiko.scalagram.types

import spray.httpx.SprayJsonSupport
import spray.json._

object ChatMemberTypes extends DefaultJsonProtocol with SprayJsonSupport {
  sealed trait ChatMemberStatus
  case object Creator extends ChatMemberStatus
  case object Administrator extends ChatMemberStatus
  case object Member extends ChatMemberStatus
  case object MemberLeft extends ChatMemberStatus
  case object MemberKicked extends ChatMemberStatus

  implicit def chatMemberTypeToStr(q: String): ChatMemberStatus = q match {
    case "creator" => Creator
    case "administrator" => Administrator
    case "member" => Member
    case "left" => MemberLeft
    case "kicked" => MemberKicked
    case _ => Member
  }

  implicit object chatmemberTypeFormat extends JsonFormat[ChatMemberStatus] {
    def write(i: ChatMemberStatus) = JsString("")
    def read(v: JsValue) = {
      val JsString(s) = v
      s
    }
  }

  import io.rockneurotiko.scalagram.types.TypesJsonSupport._
  implicit def chatMemberFormat = jsonFormat(ChatMember, "user", "status")
}

import ChatMemberTypes._

case class ChatMember(
  user: User,
  status: ChatMemberStatus)
