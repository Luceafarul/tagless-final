package devinsideyou.expressionproblem.`final`

import cats._
import cats.implicits._
object Evaluate {
  object Expression {
    def dsl[F[_]: Applicative]: Expression[F, Int] =
      new Expression[F, Int] {
        def literal(x: Int): F[Int] = x.pure[F]
        def negation(x: F[Int]): F[Int] = x.map(x => -x)
        def addition(left: F[Int], right: F[Int]): F[Int] =
          (left, right).mapN { case (x, y) => x + y }
      }
  }

  object Multiplication {
    def dsl[F[_]: Apply]: Multiplication[F, Int] =
      new Multiplication[F, Int] {
        def multiply(left: F[Int], right: F[Int]): F[Int] =
          (left, right).mapN { case (x, y) => x * y }
      }
  }

  object Division {
    type MonadErrorWithString[F[_]] = MonadError[F, String]

    def dsl[F[_]: MonadErrorWithString]: Division[F, Int] =
      new Division[F, Int] {
        def divide(left: F[Int], right: F[Int]): F[Int] =
          (left, right).tupled.flatMap {
            case (_, 0) => "Division by zero".raiseError[F, Int]
            case (x, y) => (x / y).pure[F]
          }
      }
  }
}
