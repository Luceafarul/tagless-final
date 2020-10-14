package cats

package object implicits {

  final implicit class FunctorOps[F[_] : Functor, A](private val fa: F[A]) {
    @inline def map[B](ab: A => B): F[B] = Functor[F].map(fa)(ab)
  }

  final implicit class AnyOps[A](private val a: A) extends AnyVal {
    @inline def pure[F[_] : Applicative]: F[A] = Applicative[F].pure(a)
  }

  final implicit class EqOps[A: Eq](private val x: A) {
    @inline def ===(y: A): Boolean = Eq[A].eqv(x, y)

    @inline def =!=(y: A): Boolean = Eq[A].neqv(x, y)
  }

  implicit val EqForString: Eq[String] = new Eq[String] {
    override def eqv(x: String, y: String): Boolean = x == y
  }
}
