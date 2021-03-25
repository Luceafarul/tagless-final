package com.devinsideyou.todo.crud

import com.devinsideyou.Todo
import handmade.cats.core.Functor
import handmade.cats.core.implicits._

trait Boundary[F[_]] {
  def createOne(todo: Todo.Data): F[Todo.Existing]
  def createMany(todos: Vector[Todo.Data]): F[Vector[Todo.Existing]]

  def readOneById(id: String): F[Option[Todo.Existing]]
  def readManyById(ids: Vector[String]): F[Vector[Todo.Existing]]
  def readManyByPartialDescription(partialDescription: String): F[Vector[Todo.Existing]]
  def readAll: F[Vector[Todo.Existing]]

  def updateOne(todo: Todo.Existing): F[Todo.Existing]
  def updateMany(todos: Vector[Todo.Existing]): F[Vector[Todo.Existing]]

  def deleteOne(todo: Todo.Existing): F[Unit]
  def deleteMany(todos: Vector[Todo.Existing]): F[Unit]
  def deleteAll: F[Unit]
}

object Boundary {
  def dsl[F[_]: Functor](gateway: EntityGateway[F]): Boundary[F] =
    new Boundary[F] {
      override def createOne(todo: Todo.Data): F[Todo.Existing] =
        createMany(Vector(todo)).map(_.head)

      override def createMany(todos: Vector[Todo.Data]): F[Vector[Todo.Existing]] = ???

      override def readOneById(id: String): F[Option[Todo.Existing]] = ???

      override def readManyById(ids: Vector[String]): F[Vector[Todo.Existing]] = ???

      override def readManyByPartialDescription(partialDescription: String): F[Vector[Todo.Existing]] = ???

      override def readAll: F[Vector[Todo.Existing]] = ???

      override def updateOne(todo: Todo.Existing): F[Todo.Existing] = ???

      override def updateMany(todos: Vector[Todo.Existing]): F[Vector[Todo.Existing]] = ???

      override def deleteOne(todo: Todo.Existing): F[Unit] = ???

      override def deleteMany(todos: Vector[Todo.Existing]): F[Unit] = ???

      override def deleteAll: F[Unit] = ???
    }
}
