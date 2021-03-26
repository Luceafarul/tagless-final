package handmade.cats

import handmade.cats.core.Functor

trait Apply[F[_]] extends Semigroupal[F] with Functor[F] {
  def map2[A, B, R](fa: F[A], fb: F[B])(f: (A, B) => R): F[R] =
    map(product(fa, fb))(f.tupled)
}


