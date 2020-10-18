package com.devinsideyou.todo

import cats.effect.Sync

trait Console[F[_]] {
  def getStrLn: F[String]
  def getStrLnWithPrompt(prompt: String): F[String]
  def putStrLn(line: String): F[Unit]
  def putErrLn(line: String): F[Unit]
}

object Console {
  def apply[F[_]: Console]: Console[F] = implicitly[Console[F]]

  def dsl[F[_]: Sync]: Console[F] =
    new Console[F] {

      override def getStrLn: F[String] =
        Sync[F].delay(scala.io.StdIn.readLine)

      override def getStrLnWithPrompt(prompt: String): F[String] =
        Sync[F].delay(scala.io.StdIn.readLine(prompt))

      override def putStrLn(line: String): F[Unit] =
        Sync[F].delay(println(line))

      override def putErrLn(line: String): F[Unit] =
        Sync[F].delay(scala.Console.err.println(line))
    }
}
