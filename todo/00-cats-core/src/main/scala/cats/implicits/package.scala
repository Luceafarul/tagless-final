package cats

package object implicits {

  final implicit class FunctorOps[F[_] : Functor, A](private val fa: F[A]) {
    @inline def map[B](ab: A => B): F[B] = Functor[F].map(fa)(ab)
  }

  final implicit class AnyOps[A](private val a: A) extends AnyVal {
    @inline def pure[F[_] : Applicative]: F[A] = Applicative[F].pure(a)
  }

}
