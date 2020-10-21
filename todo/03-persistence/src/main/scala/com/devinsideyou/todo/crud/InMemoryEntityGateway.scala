package com.devinsideyou.todo.crud

import cats.Monad
import cats.effect.concurrent.Ref
import cats.implicits._
import com.devinsideyou.Todo

object InMemoryEntityGateway {
  def dsl[F[_] : Monad](state: Ref[F, Vector[Todo.Existing]]): EntityGateway[F] = new EntityGateway[F] {
    private val statement: Statement[F] = Statement.dsl(state)

    override def writeMany(todos: Vector[Todo]): F[Vector[Todo.Existing]] =
      todos.traverse {
        case insert: Todo.Data => statement.insertOne(insert)
        case update: Todo.Existing => statement.updateOne(update)
      }

    override def readManyById(ids: Vector[String]): F[Vector[Todo.Existing]] =
      statement.selectAll.map(todos => todos.filter(todo => ids.contains(todo.id)))

    override def readManyByPartialDescription(partialDescription: String): F[Vector[Todo.Existing]] =
      statement.selectAll.map { todos =>
        todos.filter(
          _.description
            .toLowerCase
            .contains(partialDescription.toLowerCase)
        )
      }

    override def readAll: F[Vector[Todo.Existing]] = statement.selectAll

    override def deleteMany(todos: Vector[Todo.Existing]): F[Unit] = statement.deleteMany(todos)

    override def deleteAll: F[Unit] = statement.deleteAll
  }
}
