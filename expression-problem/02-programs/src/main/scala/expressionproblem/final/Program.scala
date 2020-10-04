package expressionproblem.initial.`final`

trait Program[F[_], A] {
  def run: F[A]
}

object Program {
  object Expression {
    def dsl[F[_], A](
        implicit
        L: Literal[F, A],
        N: Negation[F, A],
        A: Addition[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import L._, N._, A._

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
        L: Literal[F, A],
        N: Negation[F, A],
        A: Addition[F, A],
        M: Multiplication[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import L._, M._

        override val run: F[A] = multiply(
          literal(2),
          Expression.dsl.run
        )
      }
  }

  object MultiplicationInTheMiddle {
    def dsl[F[_], A](
        implicit
        L: Literal[F, A],
        N: Negation[F, A],
        A: Addition[F, A],
        M: Multiplication[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import L._, N._, A._, M._

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
        L: Literal[F, A],
        N: Negation[F, A],
        A: Addition[F, A],
        M: Multiplication[F, A],
        D: Division[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import L._, M._, D._

        override val run: F[A] = divide(
          Multiplication.dsl.run,
          literal(2)
        )
      }
  }

  object DivisionInTheMiddle {
    def dsl[F[_], A](
        implicit
        L: Literal[F, A],
        N: Negation[F, A],
        A: Addition[F, A],
        M: Multiplication[F, A],
        D: Division[F, A]
      ): Program[F, A] =
      new Program[F, A] {
        import L._, N._, A._, M._, D._

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
