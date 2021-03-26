package handmade.cats

trait Delay[F[_]] extends Applicative[F] with Defer[F] {
  def delay[A](a: => A): F[A] = defer(pure(a))
}

object Delay {
  def apply[F[_]: Delay]: Delay[F] = implicitly[Delay[F]]
}
