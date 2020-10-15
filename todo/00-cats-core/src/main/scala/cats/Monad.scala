package cats

trait Monad[F[_]] extends FlatMap[F] with Applicative[F]
