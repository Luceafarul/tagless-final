package devinsideyou.expressionproblem.`final`

trait Expr[A] {
  def literal(x: Int): A
  def negation(x: A): A
  def addition(left: A, right: A): A
}
