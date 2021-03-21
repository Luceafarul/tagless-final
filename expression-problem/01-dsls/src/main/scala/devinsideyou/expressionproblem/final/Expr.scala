package devinsideyou.expressionproblem.`final`

trait Expr[Repr] {
  def literal(x: Int): Repr
  def negation(x: Repr): Repr
  def addition(left: Repr, right: Repr): Repr
}
