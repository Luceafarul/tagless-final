package cats

trait Monad[F[_]] extends FlatMap[F] with Applicative[F] {
  final def iterateWhile[A](fa: F[A])(p: A => Boolean): F[A] =
    flatMap(fa) { a =>
      if (p(a)) iterateWhile(fa)(p)
      else pure(a)
    }
}

object Monad {
  def apply[F[_]: Monad]: Monad[F] = implicitly[Monad[F]]
}
