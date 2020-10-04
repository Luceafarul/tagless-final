package expressionproblem.initial.`final`

import cats._
import cats.syntax.all._

object View {
  object Expression {
    def dsl[F[_]: Applicative]: Expression[F, String] =
      new Expression[F, String] {
        override def literal(n: Int): F[String] = s"$n".pure[F]
        override def negation(e: F[String]): F[String] = e.map(e => s"(-$e)")
        override def addition(e1: F[String], e2: F[String]): F[String] =
          (e1, e2).mapN((e1, e2) => s"($e1 + $e2)")
      }
  }

  object Multiplication {
    def dsl[F[_]: Apply]: Multiplication[F, String] =
      new Multiplication[F, String] {
        override def multiply(e1: F[String], e2: F[String]): F[String] =
          (e1, e2).mapN((e1, e2) => s"($e1 * $e2)")
      }
  }

  object Division {
    def dsl[F[_]: Apply]: Division[F, String] =
      new Division[F, String] {
        override def divide(e1: F[String], e2: F[String]): F[String] =
          (e1, e2).mapN((e1, e2) => s"($e1 / $e2)")
      }
  }
}
