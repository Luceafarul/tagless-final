package com.devinsideyou.todo.crud

import java.time.LocalDateTime
import java.util.UUID

import com.devinsideyou.Todo
import skunk._
import skunk.implicits._
import skunk.codec.all._

object Statement {
  final implicit private class UUIDCodecOps(private val uuid: Codec[UUID]) extends AnyVal {
    def string: Codec[String] = uuid.imap(_.toString)(UUID.fromString)
  }

  final implicit private class TodoDataCodecOps(private val data: Todo.Data.type) {
    def codec: Codec[Todo.Data] =
      (text ~ timestamp).gimap[Todo.Data]
  }

  final implicit private class TodoExistingCodecOps(private val todo: Todo.Existing.type) {
    def codec: Codec[Todo.Existing] =
      (uuid.string ~ Todo.Data.codec).gimap[Todo.Existing]
  }

  object Insert {
    val one: Query[Todo.Data, Todo.Existing] =
      sql"INSERT INTO todos VALUES (${Todo.Data.codec}) RETURNING *".query(Todo.Existing.codec)

    def many(size: Int): Query[List[Todo.Data], Todo.Existing] =
      sql"INSERT INTO todos VALUES (${Todo.Data.codec.list(size)}) RETURNING *".query(Todo.Existing.codec)

    object WithUUID {
      val one: Command[Todo.Existing] =
        sql"INSERT INTO todos VALUES (${Todo.Existing.codec})".command

      def many(size: Int): Command[List[Todo.Existing]] =
        sql"INSERT INTO todos VALUES (${Todo.Existing.codec.list(size)})".command
    }
  }

  object Update {
    val one: Query[Todo.Existing, Todo.Existing] =
      sql"UPDATE todos SET description = $text, deadline = $timestamp WHERE id = ${uuid.string} RETURNING *"
        .query(Todo.Existing.codec)
        .contramap(toTwiddle)

    object Command {
      val one: Command[Todo.Existing] =
        sql"UPDATE todos SET description = $text, deadline = $timestamp WHERE id = ${uuid.string}"
          .command
          .contramap(toTwiddle)
    }

    private def toTwiddle(todo: Todo.Existing): String ~ LocalDateTime ~ String =
      todo.data.description ~ todo.data.deadline ~ todo.id
  }

  object Select {
    val all: Query[Void, Todo.Existing] =
      sql"SELECT * FROM todos".query(Todo.Existing.codec)

    def many(size: Int): Query[List[String], Todo.Existing] =
      sql"SELECT * FROM todos WHERE id IN (${uuid.string.list(size)})".query(Todo.Existing.codec)

    val byDescription: Query[String, Todo.Existing] =
      sql"SELECT * FROM todos WHERE description ~ $text".query(Todo.Existing.codec)
  }

  object Delete {
    val all: Command[Void] =
      sql"DELETE from todos".command

//    def many(ids: List[String]): Command[Void] =
//      sql"DELETE from todos WHERE id IN $ids".command

    def many(size: Int): Command[List[String]] =
      sql"DELETE from todos WHERE id IN (${uuid.string.list(size)})".command
  }
}
