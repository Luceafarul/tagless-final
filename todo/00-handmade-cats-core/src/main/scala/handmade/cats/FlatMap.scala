package handmade.cats

trait FlatMap[F[_]] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

object FlatMap {
  def apply[F[_]: FlatMap]: FlatMap[F] = implicitly[FlatMap[F]]
}
