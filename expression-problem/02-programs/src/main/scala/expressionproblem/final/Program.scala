package expressionproblem.initial.`final`

trait Program[Repr] {
  def repr: Repr
}

object Program {
  def dsl[Repr](exp: Exp[Repr]): Program[Repr] =
    new Program[Repr] {
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
}
