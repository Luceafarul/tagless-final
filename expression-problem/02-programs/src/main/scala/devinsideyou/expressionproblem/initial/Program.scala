package devinsideyou.expressionproblem.initial

object Program {
  import Expr._

  // (16 + (-(1  + 2))) = 13
  val exp: Expr = Addition(
    Literal(16),
    Negation(
      Addition(
        Literal(1),
        Literal(2)
      )
    )
  )
}