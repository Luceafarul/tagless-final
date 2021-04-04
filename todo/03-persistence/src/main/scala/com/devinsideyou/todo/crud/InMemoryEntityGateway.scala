package com.devinsideyou.todo.crud

import com.devinsideyou.Todo
import handmade.cats.core.implicits._
import handmade.cats.core.instances.EqInstances._
import handmade.cats.core.instances.TraverseInstances._
import handmade.cats.effect._

object InMemoryEntityGateway {
  def dsl[F[_]: Sync]: EntityGateway[F] =
    new EntityGateway[F] {
      private val statement: Statement[F] = Statement.dsl

      override def writeMany(todos: Vector[Todo]): F[Vector[Todo.Existing]] =
        todos.traverse {
          case create: Todo.Data     => statement.insertOne(create)
          case update: Todo.Existing => statement.updateOne(update)
        }

      override def readManyById(ids: Vector[String]): F[Vector[Todo.Existing]] =
        statement
          .selectAll
          .map(state => state.filter(todo => ids.contains(todo.id)))

      override def readManyByPartialDescription(
          partialDescription: String
        ): F[Vector[Todo.Existing]] =
        statement
          .selectAll
          .map { state =>
            state.filter(
              _.description
                .toLowerCase
                .contains(partialDescription.toLowerCase)
            )
          }

      override def readAll: F[Vector[Todo.Existing]] =
        statement.selectAll

      override def deleteMany(todos: Vector[Todo.Existing]): F[Unit] =
        statement.deleteMany(todos)

      override def deleteAll: F[Unit] =
        statement.deleteAll
    }
}
