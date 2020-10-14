package cats.effect

import cats.{Applicative, Defer}

trait Sync[F[_]] extends Applicative[F] with Defer[F] {
  def delay[A](a: => A): F[A] = defer(pure(a))
}

object Sync {
  def apply[F[_] : Sync]: Sync[F] = implicitly[Sync[F]]
}