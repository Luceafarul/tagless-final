package devinsideyou.expressionproblem.initial
import devinsideyou.expressionproblem.initial.Expr.Literal
import devinsideyou.expressionproblem.initial.Expr.Negation
import devinsideyou.expressionproblem.initial.Expr.Addition

object View {
  def interpreter(expr: Expr): String =
    expr match {
      case Literal(x)            => s"$x"
      case Negation(e)           => s"(-${interpreter(e)})"
      case Addition(left, right) => s"(${interpreter(left)} + ${interpreter(right)})"
    }

  def prefix(expr: Expr): String =
    expr match {
      case Literal(x)            => s"$x"
      case Negation(e)           => s"( - ${prefix(e)})"
      case Addition(left, right) => s"( + ${prefix(left)} ${prefix(right)})"
    }
}
