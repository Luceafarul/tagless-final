package handmade.cats.core

package object implicits {

  final implicit class FunctorOps[F[_] : Functor, A](private val fa: F[A]) {
    @inline def map[B](f: A => B): F[B] =
      Functor[F].map(fa)(f)
  }

}
