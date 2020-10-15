package cats

package object implicits {

  final implicit class FunctorOps[F[_] : Functor, A](private val fa: F[A]) {
    @inline def map[B](ab: A => B): F[B] = Functor[F].map(fa)(ab)

    @inline def as[B](b: B): F[B] = Functor[F].map(fa)(_ => b)

    @inline def void: F[Unit] = Functor[F].map(fa)(_ => ())
  }

  final implicit class FlatMapOps[F[_] : FlatMap, A](private val fa: F[A]) {
    @inline def flatMap[B](afb: A => F[B]): F[B] = FlatMap[F].flatMap(fa)(afb)

    @inline def >>[B](fb: F[B]): F[B] = FlatMap[F].flatMap(fa)(_ => fb)
  }

  final implicit class SequenceOps[F[_] : Traverse, G[_] : Applicative, A](private val fga: F[G[A]]) {
    @inline def sequence: G[F[A]] = Traverse[F].sequence(fga)
  }

  final implicit class TraverseOps[F[_] : Traverse, A](private val fa: F[A]) {
    @inline def traverse[G[_] : Applicative, B](agb: A => G[B]): G[F[B]] =
      Traverse[F].traverse(fa)(agb)
  }

  final implicit class AnyOps[A](private val a: A) extends AnyVal {
    @inline def pure[F[_] : Applicative]: F[A] = Applicative[F].pure(a)
  }

  final implicit class EqOps[A: Eq](private val x: A) {
    @inline def ===(y: A): Boolean = Eq[A].eqv(x, y)

    @inline def =!=(y: A): Boolean = Eq[A].neqv(x, y)
  }

  implicit val EqForString: Eq[String] = (x: String, y: String) => x == y

  type Id[A] = A

  implicit val traverseForVector: Traverse[Vector] = new Traverse[Vector] {
    override def map[A, B](fa: Vector[A])(ab: A => B): Vector[B] = fa.map(ab)

    override def traverse[G[_] : Applicative, A, B](fa: Vector[A])(agb: A => G[B]): G[Vector[B]] =
      fa.foldRight(Vector.empty[B].pure[G]) { (current: A, acc: G[Vector[B]]) =>
        Apply[G].map2(agb(current), acc)(_ +: _)
      }
  }

  implicit val traverseForList: Traverse[List] = new Traverse[List] {
    override def map[A, B](fa: List[A])(ab: A => B): List[B] = fa.map(ab)

    override def traverse[G[_] : Applicative, A, B](fa: List[A])(agb: A => G[B]): G[List[B]] =
      fa.foldRight(List.empty[B].pure[G]) { (current: A, acc: G[List[B]]) =>
        Apply[G].map2(agb(current), acc)(_ :: _)
      }
  }

  implicit val traverseForSet: Traverse[Set] = new Traverse[Set] {
    override def map[A, B](fa: Set[A])(ab: A => B): Set[B] = fa.map(ab)

    override def traverse[G[_] : Applicative, A, B](fa: Set[A])(agb: A => G[B]): G[Set[B]] =
      fa.foldLeft(Set.empty[B].pure[G]) { (acc: G[Set[B]], current: A) =>
        Apply[G].map2(acc, agb(current))(_ + _)
      }
  }
}
