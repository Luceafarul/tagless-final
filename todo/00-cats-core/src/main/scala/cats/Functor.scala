package cats

trait Functor[F[_]] {
  def map[A, B](fa: F[A])(ab: A => B): F[B]
}

object Functor {
  def apply[F[_]: Functor]: Functor[F] = implicitly[Functor[F]]
}
