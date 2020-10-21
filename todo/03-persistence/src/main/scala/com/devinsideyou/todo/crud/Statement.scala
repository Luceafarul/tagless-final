package com.devinsideyou.todo.crud

import cats.Monad
import cats.effect.concurrent.Ref
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
  def dsl[F[_] : Monad](state: Ref[F, Vector[Todo.Existing]]): Statement[F] = new Statement[F] {
    val nextId: F[String] =
      selectAll.map(_.size.toString)

    override def insertOne(data: Todo.Data): F[Todo.Existing] =
      nextId
        .map(id => Todo.Existing(id, data))
        .flatMap { created =>
          state.modify(s => (s :+ created) -> created)
        }

    override def updateOne(data: Todo.Existing): F[Todo.Existing] =
      state.modify(s => (s.filterNot(_.id === data.id) :+ data) -> data)

    override def selectAll: F[Vector[Todo.Existing]] =
      state.get

    override def deleteMany(todos: Vector[Todo.Existing]): F[Unit] =
      state.update(s => s.filterNot(todo => todos.map(_.id).contains(todo.id)))

    override def deleteAll: F[Unit] =
      state.set(Vector.empty)
  }
}
