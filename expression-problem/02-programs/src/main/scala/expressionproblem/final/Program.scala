package expressionproblem.initial.`final`

trait Program[A] {
  def run: Option[A]
}

object Program {
  object Expression {
    def dsl[A](expression: Expression[A]): Program[A] =
      new Program[A] {
        import expression._

        override val run: Option[A] = addition(
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
    def dsl[A](
        expression: Expression[A],
        multiplication: Multiplication[A]
      ): Program[A] =
      new Program[A] {
        import expression._
        import multiplication._

        override val run: Option[A] = multiply(
          literal(2),
          Expression.dsl(expression).run
        )
      }
  }

  object MultiplicationInTheMiddle {
    def dsl[A](
        expression: Expression[A],
        multiplication: Multiplication[A]
      ): Program[A] =
      new Program[A] {
        import expression._
        import multiplication._

        override val run: Option[A] = addition(
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
    def dsl[A](
        expression: Expression[A],
        multiplication: Multiplication[A],
        division: Division[A]
      ): Program[A] =
      new Program[A] {
        import division._
        import expression._
        import multiplication._

        override val run: Option[A] = divide(
          Multiplication.dsl(expression, multiplication).run,
          literal(2)
        )
      }
  }

  object DivisionInTheMiddle {
    def dsl[A](
        expression: Expression[A],
        multiplication: Multiplication[A],
        division: Division[A]
      ): Program[A] =
      new Program[A] {
        import division._
        import expression._
        import multiplication._

        override val run: Option[A] = addition(
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
