package expressionproblem.initial.`final`

import cats._
import cats.data._
import cats.syntax.all._
object Evaluate {
  object Literal {
    def dsl[F[_]: Applicative]: Literal[F, Int] =
      new Literal[F, Int] {
        override def literal(n: Int): F[Int] = n.pure[F]
      }
  }

  object Negation {
    def dsl[F[_]: Applicative]: Negation[F, Int] =
      new Negation[F, Int] {
        override def negation(e: F[Int]): F[Int] = e.map(-_)
      }
  }
  object Addition {
    def dsl[F[_]: Applicative: NonEmptyParallel]: Addition[F, Int] =
      new Addition[F, Int] {
        override def addition(e1: F[Int], e2: F[Int]): F[Int] =
          (e1, e2).parMapN(_ + _)
      }
  }

  object Multiplication {
    def dsl[F[_]: Apply: NonEmptyParallel]: Multiplication[F, Int] =
      new Multiplication[F, Int] {
        override def multiply(e1: F[Int], e2: F[Int]): F[Int] =
          (e1, e2).parMapN(_ * _)
      }
  }

  object Division {
    def dsl[F[_]: MonadError[*[_], NonEmptyChain[String]]: NonEmptyParallel]: Division[F, Int] =
      new Division[F, Int] {
        override def divide(e1: F[Int], e2: F[Int]): F[Int] =
          (e1, e2).parTupled.flatMap {
            case (e1, 0) => "Division by zero".pure[NonEmptyChain].raiseError[F, Int]
            case (e1, e2) =>
              if (e1 % e2 == 0) (e1 / e2).pure[F]
              else "Division ended up having rest".pure[NonEmptyChain].raiseError[F, Int]
          }
      }
  }
}
