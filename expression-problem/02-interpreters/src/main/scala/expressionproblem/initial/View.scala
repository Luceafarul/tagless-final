package expressionproblem.initial

object View {
  def apply(exp: Exp): String =
    exp match {
      case Exp.Literal(n)       => s"$n"
      case Exp.Negation(e)      => s"(-${apply(e)})"
      case Exp.Addition(e1, e2) => s"(${apply(e1)} + ${apply(e2)})"
    }
}
