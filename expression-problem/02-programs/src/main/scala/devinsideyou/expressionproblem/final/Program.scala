package devinsideyou.expressionproblem.`final`

trait Program[Repr] {
  def repr: Repr
}

object Program {
  def dsl[Repr](expr: Expr[Repr]): Program[Repr] =
    new Program[Repr] {
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
}
