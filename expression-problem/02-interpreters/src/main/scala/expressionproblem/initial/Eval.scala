package expressionproblem.initial.`final`


object Eval extends Exp[Int] {
  def literal(n: Int): Int = n
  def negation(e: Int): Int = -e
  def addition(e1: Int, e2: Int): Int = e1 + e2
}
