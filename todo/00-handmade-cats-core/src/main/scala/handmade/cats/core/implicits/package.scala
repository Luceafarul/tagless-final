package handmade.cats.core

import handmade.cats.{Applicative, Eq}

package object implicits {

  final implicit class FunctorOps[F[_]: Functor, A](private val fa: F[A]) {
    @inline def map[B](f: A => B): F[B] =
      Functor[F].map(fa)(f)
  }

  final implicit class AnyOps[A](private val a: A) extends AnyVal {
    @inline def pure[F[_]: Applicative]: F[A] = Applicative[F].pure(a)
  }

  final implicit class EqOps[A: Eq](private val a: A) {
    @inline def ===(x: A): Boolean = Eq[A].eqv(a, x)

    @inline def =!=(x: A): Boolean = Eq[A].neqv(a, x)
  }
}
