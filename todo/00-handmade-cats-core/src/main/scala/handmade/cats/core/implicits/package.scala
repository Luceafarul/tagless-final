package handmade.cats.core

package object implicits {

  final implicit class FunctorOps[F[_] : Functor, A](private val fa: F[A]) {
    @inline def map[B](f: A => B): F[B] =
      Functor[F].map(fa)(f)
  }

  final implicit class AnyOps[A](private val a: A) extends AnyVal {
    @inline def pure[F[_]: Applicative]: F[A] = Applicative[F].pure(a)
  }
}
