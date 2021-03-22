package devinsideyou.expressionproblem.`final`

trait Expression[A] {
  def literal(x: Int): A
  def negation(x: A): A
  def addition(left: A, right: A): A
}

trait Multiplication[A] {
  def multiply(left: A, right: A): A
}
