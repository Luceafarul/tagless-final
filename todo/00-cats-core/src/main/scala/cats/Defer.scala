package cats

trait Defer[F[_]] {
  def defer[A](fa: => F[A]): F[A]
}

object Defer {
  def apply[F[_]: Defer]: Defer[F] = implicitly[Defer[F]]
}
