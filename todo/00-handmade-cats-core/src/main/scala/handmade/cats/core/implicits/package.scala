package handmade.cats.core

import handmade.cats.{Applicative, Eq, FlatMap, Monad, Traverse}

package object implicits {

  final implicit class FunctorOps[F[_]: Functor, A](private val fa: F[A]) {
    @inline def map[B](f: A => B): F[B] = Functor[F].map(fa)(f)

    @inline def as[B](b: B): F[B] = Functor[F].map(fa)(_ => b)

    @inline def void: F[Unit] = Functor[F].map(fa)(_ => ())
  }

  final implicit class TraverseOps[F[_]: Traverse, A](private val fa: F[A]) {
    @inline def traverse[G[_]: Applicative, B](agb: A => G[B]): G[F[B]] =
      Traverse[F].traverse(fa)(agb)
  }

  final implicit class SequenceOps[F[_]: Traverse, G[_]: Applicative, A](private val fga: F[G[A]]) {
    @inline def sequence: G[F[A]] = Traverse[F].sequence(fga)
  }

  final implicit class FlatMapOps[F[_]: FlatMap, A](private val fa: F[A]) {
    @inline def flatMap[B](f: A => F[B]): F[B] = FlatMap[F].flatMap(fa)(f)

    @inline def >>[B](fb: => F[B]): F[B] = FlatMap[F].flatMap(fa)(_ => fb)
  }

  final implicit class MonadOps[F[_]: Monad, A](private val fa: F[A]) {
    @inline def iterateWhile(p: A => Boolean): F[A] = Monad[F].iterateWhile(fa)(p)
  }

  final implicit class AnyOps[A](private val a: A) extends AnyVal {
    @inline def pure[F[_]: Applicative]: F[A] = Applicative[F].pure(a)
  }

  final implicit class EqOps[A: Eq](private val a: A) {
    @inline def ===(x: A): Boolean = Eq[A].eqv(a, x)

    @inline def =!=(x: A): Boolean = Eq[A].neqv(a, x)
  }

}
