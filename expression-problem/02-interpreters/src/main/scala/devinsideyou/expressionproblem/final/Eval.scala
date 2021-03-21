package devinsideyou.expressionproblem.`final`

object Eval extends Expr[Int] {
  def literal(x: Int): Int = x
  def negation(x: Int): Int = -x
  def addition(left: Int, right: Int): Int = left + right
}
