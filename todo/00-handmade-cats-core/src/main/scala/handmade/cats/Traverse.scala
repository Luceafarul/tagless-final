package handmade.cats

import handmade.cats.core.Functor

trait Traverse[F[_]] extends Functor[F] {
  def sequence[G[_] : Applicative, A](fga: F[G[A]]): G[F[A]] = traverse(fga)(identity )

  def traverse[G[_] : Applicative, A, B](fa: F[A])(agb: A => G[B]): G[F[B]]
}

object Traverse {
  def apply[F[_] : Traverse]: Traverse[F] = implicitly[Traverse[F]]
}


