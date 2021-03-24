package devinsideyou.expressionproblem.`final`

import cats._
import cats.data._
import cats.implicits._
object Evaluate {
  object Literal {
    def dsl[F[_]: Applicative]: Literal[F, Int] =
      new Literal[F, Int] {
        def literal(x: Int): F[Int] = x.pure[F]
      }
  }

  object Negation {
    def dsl[F[_]: Functor]: Negation[F, Int] =
      new Negation[F, Int] {
        def negation(x: F[Int]): F[Int] = x.map(x => -x)
      }
  }

  object Addition {
    def dsl[F[_]: Apply: NonEmptyParallel]: Addition[F, Int] =
      new Addition[F, Int] {
        def addition(left: F[Int], right: F[Int]): F[Int] =
          (left, right).parMapN { case (x, y) => x + y }
      }
  }

  object Multiplication {
    def dsl[F[_]: Apply: NonEmptyParallel]: Multiplication[F, Int] =
      new Multiplication[F, Int] {
        def multiply(left: F[Int], right: F[Int]): F[Int] =
          (left, right).parMapN { case (x, y) => x * y }
      }
  }

  object Division {
    type MonadErrorWithString[F[_]] = MonadError[F, String]

    def dsl[F[_]: MonadError[*[_], NonEmptyChain[String]]: NonEmptyParallel]: Division[F, Int] =
      new Division[F, Int] {
        def divide(left: F[Int], right: F[Int]): F[Int] =
          (left, right).parTupled.flatMap {
            case (_, 0) => "Division by zero".pure[NonEmptyChain].raiseError[F, Int]
            case (x, y) if (x % y == 0) => (x / y).pure[F]
            case (x, y) => s"Division with rest: ${x % y}".pure[NonEmptyChain].raiseError[F, Int]
          }
      }
  }
}
