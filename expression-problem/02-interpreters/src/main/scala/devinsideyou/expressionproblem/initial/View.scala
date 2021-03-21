package devinsideyou.expressionproblem.initial
import devinsideyou.expressionproblem.initial.Expr.Literal
import devinsideyou.expressionproblem.initial.Expr.Negation
import devinsideyou.expressionproblem.initial.Expr.Addition

object View {
  def apply(expr: Expr): String =
    expr match {
      case Literal(x)            => s"$x"
      case Negation(e)           => s"(-${apply(e)})"
      case Addition(left, right) => s"(${apply(left)} + ${apply(right)})"
    }

  def prefix(expr: Expr): String =
    expr match {
      case Literal(x)            => s"$x"
      case Negation(e)           => s"( - ${apply(e)})"
      case Addition(left, right) => s"( + ${apply(left)} ${apply(right)})"
    }
}
