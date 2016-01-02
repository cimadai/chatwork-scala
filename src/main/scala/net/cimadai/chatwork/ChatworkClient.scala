package net.cimadai.chatwork

import dispatch._

// cf: http://developer.chatwork.com/ja/endpoints.html

class ChatworkClient(apiKey: String) extends HttpClient {
  private val baseUrl = "https://api.chatwork.com/v1"

  override def decorateRequest(req: Req): Req = {
    req.addHeader("X-ChatWorkToken", apiKey)
  }

  private def uri(uri: String) = baseUrl + uri

  def me(): Either[Option[JsonErrors], Me] =
    getAs[JsonErrors, Me](uri("/me"))

  def myStatus(): Either[Option[JsonErrors], Status] =
    getAs[JsonErrors, Status](uri("/my/status"))

  def contacts(): Either[Option[JsonErrors], List[Contact]] =
    getAs[JsonErrors, List[Contact]](uri("/contacts"))

  def rooms(): Either[Option[JsonErrors], List[Room]] =
    getAs[JsonErrors, List[Room]](uri("/rooms"))

  def room(roomId: String): Either[Option[JsonErrors], Room] =
    getAs[JsonErrors, Room](uri(s"/rooms/$roomId"))

  def roomMembers(roomId: String): Either[Option[JsonErrors], List[Member]] =
    getAs[JsonErrors, List[Member]](uri(s"/rooms/$roomId/members"))

  def roomMessages(roomId: String): Either[Option[JsonErrors], List[Message]] =
    getAs[JsonErrors, List[Message]](uri(s"/rooms/$roomId/messages"))

  def roomMessage(roomId: String, messageId: String): Either[Option[JsonErrors], Message] =
    getAs[JsonErrors, Message](uri(s"/rooms/$roomId/messages/$messageId"))

  def roomTasks(roomId: String): Either[Option[JsonErrors], List[Task]] =
    getAs[JsonErrors, List[Task]](uri(s"/rooms/$roomId/tasks"))

  def roomTask(roomId: String, taskId: String): Either[Option[JsonErrors], Task] =
    getAs[JsonErrors, Task](uri(s"/rooms/$roomId/tasks/$taskId"))

  def roomFile(roomId: String, fileId: String): Either[Option[JsonErrors], File] =
    getAs[JsonErrors, File](uri(s"/rooms/$roomId/files/$fileId"))

  def roomFiles(roomId: String, accountId: String): Either[Option[JsonErrors], List[File]] = {
    val params: Params = Map("account_id" -> Seq(accountId))
    getAs[JsonErrors, List[File]](uri(s"/rooms/$roomId/files"), Some(params))
  }

  def myTasks(assignedByAccountIdOrNone: Option[String] = None, statusOrNone: Option[TaskStatus.Value] = None): Either[Option[JsonErrors], List[MyTask]] = {
    val params =
      assignedByAccountIdOrNone.map(assignedByAccountId => Map("assigned_by_account_id" -> Seq(assignedByAccountId))).getOrElse(Map.empty) ++
        statusOrNone.map(status => Map("status" -> Seq(status.toString))).getOrElse(Map.empty)
    getAs[JsonErrors, List[MyTask]](uri("/my/tasks"), Some(params))
  }

  def createRoom(name: String, membersAdminIds: List[Int], descriptionOrNone: Option[String] = None,
    iconPresetOrNone: Option[RoomIcon.Value] = None, membersMemberIds: List[Int] = List.empty,
    membersReadonlyIds: List[Int] = List.empty): Either[Option[JsonErrors], RoomIdResponse] = {
    val params =
      Map("name" -> Seq(name), "members_admin_ids" -> Seq(membersAdminIds.mkString(",")))
    descriptionOrNone.map(value => Map("description" -> Seq(value))).getOrElse(Map.empty) ++
      iconPresetOrNone.map(value => Map("icon_preset" -> Seq(value))).getOrElse(Map.empty) ++
      (if (membersMemberIds.nonEmpty) { Map("members_member_ids" -> Seq(membersMemberIds.mkString(","))) } else { Map.empty }) ++
      (if (membersReadonlyIds.nonEmpty) { Map("members_readonly_ids" -> Seq(membersReadonlyIds.mkString(","))) } else { Map.empty })
    postAs[JsonErrors, RoomIdResponse](uri("/rooms"), params)
  }

  def updateRoom(roomId: String, description: String, iconPreset: RoomIcon.Value): Either[Option[JsonErrors], RoomIdResponse] = {
    val params =
      Map("description" -> Seq(description), "icon_preset" -> Seq(iconPreset.toString))
    putAs[JsonErrors, RoomIdResponse](uri(s"/rooms/$roomId"), params)
  }

  def updateRoomMembers(roomId: String, membersAdminIds: List[Int], membersMemberIds: List[Int] = List.empty,
    membersReadonlyIds: List[Int] = List.empty): Either[Option[JsonErrors], RoomMemberUpdateResponse] = {
    val params =
      Map("members_admin_ids" -> Seq(membersAdminIds.mkString(",")))
    (if (membersMemberIds.nonEmpty) { Map("members_member_ids" -> Seq(membersMemberIds.mkString(","))) } else { Map.empty }) ++
      (if (membersReadonlyIds.nonEmpty) { Map("members_readonly_ids" -> Seq(membersReadonlyIds.mkString(","))) } else { Map.empty })
    putAs[JsonErrors, RoomMemberUpdateResponse](uri(s"/rooms/$roomId/members"), params)
  }

  def deleteRoom(roomId: String, actionType: ActionType.Value): Either[Option[JsonErrors], Boolean] = {
    val params = Map("action_type" -> Seq(actionType.toString))
    delete(uri(s"/rooms/$roomId"), params) match {
      case Some(response) => Right(true)
      case _ => Left(None)
    }
  }

  def postRoomMessage(roomId: String, message: String): Either[Option[JsonErrors], MessageIdResponse] = {
    val params =
      Map("body" -> Seq(message))
    postAs[JsonErrors, MessageIdResponse](uri(s"/rooms/$roomId/messages"), params)
  }

  def postRoomTask(roomId: String, body: String, toIds: List[Int], limitUnixTimeOrNone: Option[Long]): Either[Option[JsonErrors], TaskIdsResponse] = {
    val params =
      Map("body" -> Seq(body), "to_ids" -> Seq(toIds.mkString(","))) ++
        limitUnixTimeOrNone.map(value => Map("limit" -> Seq(value.toString))).getOrElse(Map.empty)
    postAs[JsonErrors, TaskIdsResponse](uri(s"/rooms/$roomId/tasks"), params)
  }
}

