package devinsideyou.expressionproblem.initial
import devinsideyou.expressionproblem.initial.Expr.Literal
import devinsideyou.expressionproblem.initial.Expr.Negation
import devinsideyou.expressionproblem.initial.Expr.Addition

object Eval {
  def apply(expr: Expr): Int =
    expr match {
      case Literal(x)            => x
      case Negation(e)           => -apply(e)
      case Addition(left, right) => apply(left) + apply(right)
    }
}
