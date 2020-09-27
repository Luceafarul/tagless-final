package expressionproblem.initial

object Eval {
  def apply(exp: Exp): Int =
    exp match {
      case Exp.Literal(n)       => n
      case Exp.Negation(e)      => -apply(e)
      case Exp.Addition(e1, e2) => apply(e1) + apply(e2)
    }
}
