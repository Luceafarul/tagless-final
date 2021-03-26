package com.devinsideyou.todo.crud

import com.devinsideyou.Todo
import handmade.cats.effect._

object InMemoryEntityGateway {
  def dsl[F[_] : Sync]: EntityGateway[F] = new EntityGateway[F] {
    var nextId: Int = 0
    var state: Vector[Todo.Existing] = Vector.empty

    override def writeMany(todos: Vector[Todo]): F[Vector[Todo.Existing]] = ???

    override def readManyById(ids: Vector[String]): F[Vector[Todo.Existing]] = ???

    override def readManyByPartialDescription(partialDescription: String): F[Vector[Todo.Existing]] = ???

    override def readAll: F[Vector[Todo.Existing]] = ???

    override def deleteMany(todos: Vector[Todo.Existing]): F[Unit] = ???

    override def deleteAll: F[Unit] = ???

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
      state = state.filterNot(_.id == todo.id) :+ todo

      todo
    }
  }
}
