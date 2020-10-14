package com.devinsideyou.todo.crud

import com.devinsideyou.Todo
import cats.effect._
import cats.implicits._

object InMemoryEntityGateway {
  def dsl[F[_] : Sync]: EntityGateway[F] = new EntityGateway[F] {
    var nextId: Int = 0
    var state: Vector[Todo.Existing] = Vector.empty

    override def writeMany(todos: Vector[Todo]): F[Vector[Todo.Existing]] =
      todos.traverse(writeOne)

    override def readManyById(ids: Vector[String]): F[Vector[Todo.Existing]] = Sync[F].delay {
      state.filter(todo => ids.contains(todo.id))
    }

    override def readManyByPartialDescription(partialDescription: String): F[Vector[Todo.Existing]] = Sync[F].delay {
      state.filter(
        _.description
          .toLowerCase
          .contains(partialDescription.toLowerCase)
      )
    }

    override def readAll: F[Vector[Todo.Existing]] = Sync[F].delay {
      state
    }

    override def deleteMany(todos: Vector[Todo.Existing]): F[Unit] = Sync[F].delay {
      state = state.filterNot(todo => todos.map(_.id).contains(todo.id))
    }

    override def deleteAll: F[Unit] = Sync[F].delay {
      state = Vector.empty
    }

    private def createOne(todo: Todo.Data): F[Todo.Existing] = Sync[F].delay {
      val created =
        Todo.Existing(
          id = nextId.toString,
          data = todo
        )

      state :+= created

      nextId += 1

      created
    }

    private def updateOne(todo: Todo.Existing): F[Todo.Existing] = Sync[F].delay {
      state = state.filterNot(_.id === todo.id) :+ todo
    }.as(todo)

    private def writeOne(todo: Todo): F[Todo.Existing] =
      todo match {
        case item: Todo.Data => createOne(item)
        case item: Todo.Existing => updateOne(item)
      }
  }
}
