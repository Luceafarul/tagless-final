package handmade.cats.core

import handmade.cats.core.implicits.AnyOps
import handmade.cats.{Applicative, Eq, Traverse}

package object instances {
  object EqInstances {
    implicit val eqString: Eq[String] = (x: String, y: String) => x == y
  }

  object TraverseInstances {
    implicit val vectorTraverse: Traverse[Vector] = new Traverse[Vector] {
      override def traverse[G[_]: Applicative, A, B](fa: Vector[A])(agb: A => G[B]): G[Vector[B]] =
        fa.foldRight(Vector.empty[B].pure[G]) { (current: A, acc: G[Vector[B]]) =>
          val gb = agb(current)

          Applicative[G].map2(gb, acc)((b, vector) => vector :+ b)
        }

      override def map[A, B](fa: Vector[A])(f: A => B): Vector[B] = fa.map(f)
    }
  }
}
