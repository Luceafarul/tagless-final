package expressionproblem.initial.`final`

trait Program[A] {
  def run: A
}

trait OptionProgram[A] {
  def run: Option[A]
}

object Program {
  object Expression {
    def dsl[A](expression: Expression[A]): Program[A] =
      new Program[A] {
        import expression._

        override val run: A = addition(
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

        override val run: A = multiply(
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

        override val run: A = addition(
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
      ): OptionProgram[A] =
      new OptionProgram[A] {
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
      ): OptionProgram[A] =
      new OptionProgram[A] {
        import division._
        import expression._
        import multiplication._

        override val run: Option[A] = {
          val resultOfDivision = divide(
            multiply(
              literal(2),
              addition(
                literal(1),
                literal(2)
              )
            ),
            literal(2)
          )

          resultOfDivision.map(res =>
            addition(
              literal(16),
              negation(
                res
              )
            )
          )
        }
      }
  }
}
