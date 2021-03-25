package handmade.cats.core

trait Applicative[F[_]] extends Functor[F] {
  def pure[A](a: A): F[A]
}

object Applicative {
  def apply[F[_]: Applicative]: Applicative[F] = implicitly[Applicative[F]]
}
