package net.cimadai.chatwork

import org.scalatest.FunSpec

class HttpClientSpec extends FunSpec {
  private val apiKey = "" // please specify your api key for test.

  describe("dispatch getting-started spec") {
    it("api can work") {
      if (apiKey.nonEmpty) {
        val cli = new ChatworkClient(apiKey)

        val meOrError = cli.me()
        assert(meOrError.isRight, true)
        val me = meOrError.right.get

        assert(cli.myStatus().isRight, true)
        assert(cli.contacts().isRight, true)

        val roomsOrError = cli.rooms()
        assert(roomsOrError.isRight, true)
        roomsOrError match {
          case Right(rooms) =>
            if (rooms.nonEmpty) {
              val firstRoomId = rooms.head.roomId.toString
              assert(cli.room(firstRoomId).isRight, true)

              assert(cli.roomMembers(firstRoomId).isRight, true)

              val roomMessagesOrError = cli.roomMessages(firstRoomId)
              assert(roomMessagesOrError.isRight, true)
              val roomMessages = roomMessagesOrError.right.get
              if (roomMessages.nonEmpty) {
                val firstRoomMessageId = roomMessages.head.messageId.toString
                assert(cli.roomMessage(firstRoomId, firstRoomMessageId).isRight, true)
              }

              val roomTasksOrError = cli.roomTasks(firstRoomId)
              assert(roomTasksOrError.isRight, true)
              val roomTasks = roomTasksOrError.right.get
              if (roomTasks.nonEmpty) {
                val firstRoomTaskId = roomTasks.head.taskId.toString
                assert(cli.roomTask(firstRoomId, firstRoomTaskId).isRight, true)
              }

              val roomFilesOrError = cli.roomFiles(firstRoomId, me.accountId.toString)
              assert(roomFilesOrError.isRight, true)
              val roomFiles = roomFilesOrError.right.get
              if (roomFiles.nonEmpty) {
                assert(cli.roomFile(firstRoomId, roomFiles.head.fileId.toString).isRight, true)
              }
            }
          case Left(_) => fail("Failed to get rooms.")
        }
      }
    }
  }
}
