package com.devinsideyou.todo.crud

import cats.effect.Sync
import cats.implicits._
import com.devinsideyou.Todo

trait Statement[F[_]] {
  def insertOne(data: Todo.Data): F[Todo.Existing]
  def updateOne(data: Todo.Existing): F[Todo.Existing]
  def selectAll: F[Vector[Todo.Existing]]
  def deleteMany(todos: Vector[Todo.Existing]): F[Unit]
  def deleteAll: F[Unit]
}

object Statement {
  def dsl[F[_] : Sync]: Statement[F] = new Statement[F] {
    var state: Vector[Todo.Existing] = Vector.empty

    val nextId: F[String] = Sync[F].delay(state.size.toString)

    override def insertOne(data: Todo.Data): F[Todo.Existing] =
      nextId
        .map(id => Todo.Existing(id, data))
        .flatMap { created =>
          Sync[F].delay(state :+= created).as(created)
        }

    override def updateOne(data: Todo.Existing): F[Todo.Existing] = Sync[F].delay {
      state = state.filterNot(_.id === data.id) :+ data
    }.as(data)

    override def selectAll: F[Vector[Todo.Existing]] = ???

    override def deleteMany(todos: Vector[Todo.Existing]): F[Unit] = Sync[F].delay {
      state = state.filterNot(todo => todos.map(_.id).contains(todo.id))
    }

    override def deleteAll: F[Unit] = Sync[F].delay {
      state = Vector.empty
    }
  }
}
