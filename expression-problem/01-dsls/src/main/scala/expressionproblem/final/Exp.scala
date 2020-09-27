package expressionproblem.initial.`final`

trait Exp[Repr] {
  def literal(n: Int): Repr
  def negation(e: Repr): Repr
  def addition(e1: Repr, e2: Repr): Repr
}
