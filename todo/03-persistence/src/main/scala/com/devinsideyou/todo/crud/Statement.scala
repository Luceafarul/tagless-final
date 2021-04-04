package com.devinsideyou.todo.crud

import com.devinsideyou.Todo
import handmade.cats.core.implicits._
import handmade.cats.core.instances.EqInstances._
import handmade.cats.effect.Sync

trait Statement[F[_]] {
  def insertOne(todo: Todo.Data): F[Todo.Existing]
  def updateOne(todo: Todo.Existing): F[Todo.Existing]
  def selectAll: F[Vector[Todo.Existing]]
  def deleteMany(todos: Vector[Todo.Existing]): F[Unit]
  def deleteAll: F[Unit]
}

object Statement {
  def dsl[F[_]: Sync]: Statement[F] =
    new Statement[F] {
      private var state: Vector[Todo.Existing] = Vector.empty
      private val nextId: F[String] = Sync[F].delay(state.size.toString)

      override def insertOne(todo: Todo.Data): F[Todo.Existing] =
        nextId
          .map(id => Todo.Existing(id, todo))
          .flatMap { created =>
            Sync[F].delay(state :+= created).as(created)
          }

      override def updateOne(todo: Todo.Existing): F[Todo.Existing] =
        Sync[F]
          .delay { state = state.filterNot(_.id === todo.id) :+ todo }
          .as(todo)

      override val selectAll: F[Vector[Todo.Existing]] = Sync[F].delay(state)

      override def deleteMany(todos: Vector[Todo.Existing]): F[Unit] =
        Sync[F].delay { state = state.filterNot(todo => todos.map(_.id).contains(todo.id)) }


      override val deleteAll: F[Unit] =
        Sync[F].delay { state = Vector.empty }.void
    }
}
