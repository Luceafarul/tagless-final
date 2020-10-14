package cats

trait Semigroupal[F[_]] {
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
}

object Semigroupal {
  def apply[F[_] : Semigroupal]: Semigroupal[F] = implicitly[Semigroupal[F]]
}
