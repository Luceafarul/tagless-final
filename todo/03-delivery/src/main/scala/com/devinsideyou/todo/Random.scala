package com.devinsideyou.todo

import handmade.cats.effect.Sync

trait Random[F[_]] {
  def nextInt(n: Int): F[Int]
}

object Random {
  def apply[F[_]: Random]: Random[F] = implicitly[Random[F]]

  implicit def dsl[F[_]: Sync]: Random[F] = (n: Int) => Sync[F].delay(scala.util.Random.nextInt(n))
}
