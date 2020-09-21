package expressionproblem.initial

object Program {
  import Exp._

  // (16 + (-(1 + 2)))
  val exp: Exp = Addition(
    Literal(16),
    Negation(
      Addition(
        Literal(1),
        Literal(2)
      )
    )
  )
}
