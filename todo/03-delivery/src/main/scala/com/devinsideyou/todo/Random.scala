package com.devinsideyou.todo

import cats.effect._

trait Random[F[_]] {
  def nextInt(n: Int): F[Int]
}

object Random {
  def apply[F[_] : Random]: Random[F] = implicitly[Random[F]]

  def dsl[F[_] : Sync]: F[Random[F]] = Sync[F].delay {
    (n: Int) => Sync[F].delay(scala.util.Random.nextInt(n))
  }
}
