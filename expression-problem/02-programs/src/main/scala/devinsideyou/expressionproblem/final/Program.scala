package devinsideyou.expressionproblem.`final`

trait Program[A] {
  def run: A
}

object Program {
  object Expression {
    def dsl[A](expr: Expression[A]): Program[A] =
      new Program[A] {
        import expr._

        val run: A = addition(
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
        expr: Expression[A],
        multiplication: Multiplication[A]
      ): Program[A] =
      new Program[A] {
        import multiplication._
        import expr._

        val run: A = multiply(
          literal(2),
          Expression.dsl(expr).run
        )
      }
  }

  object MultiplicationInTheMiddle {
    def dsl[A](
        expr: Expression[A],
        multiplication: Multiplication[A]
      ): Program[A] =
      new Program[A] {
        import multiplication._
        import expr._

        val run: A = addition(
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
}
