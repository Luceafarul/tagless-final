package devinsideyou.expressionproblem.`final`

class Program[Repr](expr: Expr[Repr]) {
  import expr._

  val repr: Repr = addition(
    literal(16),
    negation(
      addition(
        literal(1),
        literal(2)
      )
    )
  )
}
