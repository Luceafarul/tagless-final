package cats

trait FlatMap[F[_]] {
  def flatMap[A, B](fa: F[A])(ab: A => F[B]): F[B]
}

object FlatMap {
  def apply[F[_] : FlatMap]: FlatMap[F] = implicitly[FlatMap[F]]
}
