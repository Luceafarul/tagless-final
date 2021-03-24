package devinsideyou.expressionproblem.`final`

trait Program[F[_], A] {
  def run: F[A]
}

object Program {
  object Expression {
    def dsl[F[_], A](
        implicit
        lit: Literal[F, A],
        neg: Negation[F, A],
        add: Addition[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import lit._
        import neg._
        import add._

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
        implicit
        lit: Literal[F, A],
        neg: Negation[F, A],
        add: Addition[F, A],
        mul: Multiplication[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import lit._
        import neg._
        import add._
        import mul._

        override val run: F[A] = multiply(
          literal(2),
          Expression.dsl.run
        )
      }
  }

  object MultiplicationInTheMiddle {
    def dsl[F[_], A](
        implicit
        lit: Literal[F, A],
        neg: Negation[F, A],
        add: Addition[F, A],
        mul: Multiplication[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import lit._
        import neg._
        import add._
        import mul._

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
        implicit
        lit: Literal[F, A],
        neg: Negation[F, A],
        add: Addition[F, A],
        mul: Multiplication[F, A],
        div: Division[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import lit._
        import div._
        override val run: F[A] = divide(
          Multiplication.dsl.run,
          literal(2)
        )
      }
  }

  object DivisionInTheMiddle {
    def dsl[F[_], A](
        implicit
        lit: Literal[F, A],
        neg: Negation[F, A],
        add: Addition[F, A],
        mul: Multiplication[F, A],
        div: Division[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import lit._
        import neg._
        import add._
        import mul._
        import div._
        override val run: F[A] =
          addition(
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
