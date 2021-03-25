package com.devinsideyou.todo.crud

import com.devinsideyou.Todo

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


