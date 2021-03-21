package devinsideyou.expressionproblem.initial
import devinsideyou.expressionproblem.initial.Expr.Literal
import devinsideyou.expressionproblem.initial.Expr.Negation
import devinsideyou.expressionproblem.initial.Expr.Addition

object Eval {
  def interpreter(expr: Expr): Int =
    expr match {
      case Literal(x)            => x
      case Negation(e)           => -interpreter(e)
      case Addition(left, right) => interpreter(left) + interpreter(right)
    }
}
