package handmade.cats

trait Applicative[F[_]] extends Apply[F] {
  def pure[A](a: A): F[A]
}

object Applicative {
  def apply[F[_]: Applicative]: Applicative[F] = implicitly[Applicative[F]]
}
