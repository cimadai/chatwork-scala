package net.cimadai.chatwork

case class Me(
  accountId: Int, roomId: Int, name: String, chatworkId: String, organizationId: Int, organizationName: String,
  department: String, title: String, url: String, introduction: String, mail: String, telOrganization: String,
  telExtension: String, telMobile: String, skype: String, facebook: String, twitter: String, avatarImageUrl: String)

case class Task(taskId: Int, account: Account, assignedByAccount: Account, messageId: Int, body: String, limitTime: Long, status: String)

case class Status (unreadRoomNum: Int, mentionRoomNum: Int, mytaskRoomNum: Int, unreadNum: Int, mentionNum: Int, mytaskNum: Int)

case class MyTask (taskId: Int, account: Account, assignedByAccount: Account, messageId: Int, body: String, limitTime: Long, status: String, roomId: Int, name: String, iconPath: String)

case class Account(accountId: Int, name: String, avatarImageUrl: String)

case class Contact(accountId: Int, roomId: Int, name: String, chatworkId: String, organizationId: Int, organizationName: String, department: String, avatarImageUrl: String)

case class Room (roomId: Int, name: String, `type`: String, role: String, sticky: Boolean, unreadNum: Int, mentionNum: Int, mytaskNum: Int, messageNum: Int, fileNum: Int, taskNum: Int, iconPath: String, lastUpdateTime: Long)

case class Member (accountId: Int, role: String, name: String, chatworkId: String, organizationId: Int, organizationName: String, department: String, avatarImageUrl: String)

case class Message (messageId: Int, account: Account, body: String, sendTime: Long, updateTime: Long)

case class PostResponse (messageId: Int)

case class File (fileId: Int, account: Account, messageId: Int, filename: String, filesize: Int, uploadTime: Long)

case class JsonErrors(errors: List[String])

object TaskStatus extends Enumeration {
  val open, done = Value
}

object RoomIcon extends Enumeration {
  val group, check, document, meeting, event, project, business, study, security, star, idea, heart, magcup, beer, music, sports, travel = Value
}

object ActionType extends Enumeration {
  val leave, delete = Value
}

case class RoomIdResponse(roomId: Int)

case class RoomMemberUpdateResponse(admin: List[Int], member: List[Int], readonly: List[Int])

case class MessageIdResponse(messageId: Int)

case class TaskIdsResponse(taskIds: List[Int])

