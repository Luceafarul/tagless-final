package devinsideyou.expressionproblem.`final`

trait Program[A] {
  def run: A
}

trait ProgramOption[A] {
  def run: Option[A]
}

object Program {
  object Expression {
    def dsl[A](implicit expression: Expression[A]): Program[A] =
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
        implicit
        expression: Expression[A],
        multiplication: Multiplication[A]
      ): Program[A] =
      new Program[A] {
        import multiplication._
        import expression._

        override val run: A = multiply(
          literal(2),
          Expression.dsl.run
        )
      }
  }

  object MultiplicationInTheMiddle {
    def dsl[A](
        implicit
        expression: Expression[A],
        multiplication: Multiplication[A]
      ): Program[A] =
      new Program[A] {
        import multiplication._
        import expression._

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
        implicit
        expression: Expression[A],
        multiplication: Multiplication[A],
        division: Division[A]
      ): ProgramOption[A] =
      new ProgramOption[A] {
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
        expression: Expression[A],
        multiplication: Multiplication[A],
        division: Division[A]
      ): ProgramOption[A] =
      new ProgramOption[A] {
        import multiplication._
        import expression._
        import division._

        override val run: Option[A] = {
          val divisionResult: Option[A] =
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

          divisionResult.map { result =>
            addition(
              literal(16),
              negation(
                result
              )
            )
          }
        }
      }
  }
}
