package expressionproblem.initial.`final`

trait Program[F[_], A] {
  def run: F[A]
}

object Program {
  object Expression {
    def dsl[F[_], A](expression: Expression[F, A]): Program[F, A] =
      new Program[F, A] {
        import expression._

        override val run: F[A] = addition(
          literal(16),
          negation(
            addition(
              literal(1),
              literal(2)
            )
          )
        )
      }
  }

  object Multiplication {
    def dsl[F[_], A](
        expression: Expression[F, A],
        multiplication: Multiplication[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import expression._
        import multiplication._

        override val run: F[A] = multiply(
          literal(2),
          Expression.dsl(expression).run
        )
      }
  }

  object MultiplicationInTheMiddle {
    def dsl[F[_], A](
        expression: Expression[F, A],
        multiplication: Multiplication[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import expression._
        import multiplication._

        override val run: F[A] = addition(
          literal(16),
          negation(
            multiply(
              literal(2),
              addition(
                literal(1),
                literal(2)
              )
            )
          )
        )
      }
  }

  object Division {
    def dsl[F[_], A](
        expression: Expression[F, A],
        multiplication: Multiplication[F, A],
        division: Division[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import division._
        import expression._
        import multiplication._

        override val run: F[A] = divide(
          Multiplication.dsl(expression, multiplication).run,
          literal(2)
        )
      }
  }

  object DivisionInTheMiddle {
    def dsl[F[_], A](
        implicit
        expression: Expression[F, A],
        multiplication: Multiplication[F, A],
        division: Division[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import division._
        import expression._
        import multiplication._

        override val run: F[A] = addition(
          literal(16),
          negation(
            divide(
              multiply(
                literal(2),
                addition(
                  literal(1),
                  literal(2)
                )
              ),
              literal(2)
            )
          )
        )
      }
  }
}
