package expressionproblem.initial.`final`

class Program[Repr](exp: Exp[Repr]) {
  import exp._

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
