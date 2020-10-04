package expressionproblem.initial.`final`

import cats._
import cats.syntax.all._

object View {
  object Literal {
    def dsl[F[_]: Applicative]: Literal[F, String] =
      new Literal[F, String] {
        override def literal(n: Int): F[String] = s"$n".pure[F]
      }
  }

  object Negation {
    def dsl[F[_]: Applicative]: Negation[F, String] =
      new Negation[F, String] {
        override def negation(e: F[String]): F[String] = e.map(e => s"(-$e)")
      }
  }

  object Addition {
    def dsl[F[_]: Applicative: NonEmptyParallel]: Addition[F, String] =
      new Addition[F, String] {
        override def addition(e1: F[String], e2: F[String]): F[String] =
          (e1, e2).parMapN((e1, e2) => s"($e1 + $e2)")
      }
  }
  object Multiplication {
    def dsl[F[_]: Apply: NonEmptyParallel]: Multiplication[F, String] =
      new Multiplication[F, String] {
        override def multiply(e1: F[String], e2: F[String]): F[String] =
          (e1, e2).parMapN((e1, e2) => s"($e1 * $e2)")
      }
  }

  object Division {
    def dsl[F[_]: Apply: NonEmptyParallel]: Division[F, String] =
      new Division[F, String] {
        override def divide(e1: F[String], e2: F[String]): F[String] =
          (e1, e2).parMapN((e1, e2) => s"($e1 / $e2)")
      }
  }
}
