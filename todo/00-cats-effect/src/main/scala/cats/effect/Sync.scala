package cats.effect

import cats.{Defer, Monad}

trait Sync[F[_]] extends Monad[F] with Defer[F] {
  def delay[A](a: => A): F[A] = defer(pure(a))
}

object Sync {
  def apply[F[_] : Sync]: Sync[F] = implicitly[Sync[F]]
}
