package devinsideyou.expressionproblem.`final`

import cats._
import cats.syntax.all._

object View {
  object Expression {
    def dsl[F[_]: Applicative]: Expression[F, String] =
      new Expression[F, String] {
        def literal(x: Int): F[String] = s"$x".pure[F]
        def negation(x: F[String]): F[String] = x.map(x => s"(-$x)")
        def addition(left: F[String], right: F[String]): F[String] =
          (left, right).mapN { case (left, right) => s"($left + $right)" }
      }
  }

  object Multiplication {
    def dsl[F[_]: Apply]: Multiplication[F, String] =
      new Multiplication[F, String] {
        def multiply(left: F[String], right: F[String]): F[String] =
          (left, right).mapN { case (left, right) => s"($left * $right)" }
      }
  }

  object Division {
    def dsl[F[_]: FlatMap]: Division[F, String] =
      new Division[F, String] {
        def divide(left: F[String], right: F[String]): F[String] =
          for {
            l <- left
            r <- right
          } yield s"($l / $r)"
      }
  }
}
