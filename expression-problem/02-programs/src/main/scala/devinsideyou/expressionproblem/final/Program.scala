package devinsideyou.expressionproblem.`final`

trait Program[A] {
  def run: Option[A]
}

object Program {
  object Expression {
    def dsl[A](implicit expression: Expression[Option, A]): Program[A] =
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
        implicit
        expression: Expression[Option, A],
        multiplication: Multiplication[Option, A]
      ): Program[A] =
      new Program[A] {
        import multiplication._
        import expression._

        override val run: Option[A] = multiply(
          literal(2),
          Expression.dsl.run
        )
      }
  }

  object MultiplicationInTheMiddle {
    def dsl[A](
        implicit
        expression: Expression[Option, A],
        multiplication: Multiplication[Option, A]
      ): Program[A] =
      new Program[A] {
        import multiplication._
        import expression._

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
        implicit
        expression: Expression[Option, A],
        multiplication: Multiplication[Option, A],
        division: Division[Option, A]
      ): Program[A] =
      new Program[A] {
        import multiplication._
        import expression._
        import division._

        override val run: Option[A] = divide(
          Multiplication.dsl.run,
          literal(2)
        )
      }
  }

  object DivisionInTheMiddle {
    def dsl[A](
        implicit
        expression: Expression[Option, A],
        multiplication: Multiplication[Option, A],
        division: Division[Option, A]
      ): Program[A] =
      new Program[A] {
        import multiplication._
        import expression._
        import division._

        override val run: Option[A] =
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
