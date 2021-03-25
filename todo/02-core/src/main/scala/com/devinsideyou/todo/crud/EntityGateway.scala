package com.devinsideyou.todo.crud

import com.devinsideyou.Todo

trait EntityGateway[F[_]] {
  def writeMany(todos: Vector[Todo]): F[Vector[Todo.Existing]]

  def readManyById(ids: Vector[String]): F[Vector[Todo.Existing]]
  def readManyByPartialDescription(partialDescription: String): F[Vector[Todo.Existing]]
  def readAll: F[Vector[Todo.Existing]]

  def deleteMany(todos: Vector[Todo.Existing]): F[Unit]
  def deleteAll: F[Unit]
}
