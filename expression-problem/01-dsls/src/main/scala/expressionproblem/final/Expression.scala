package expressionproblem.initial.`final`

trait Expression[A] {
  def literal(n: Int): A
  def negation(e: A): A
  def addition(e1: A, e2: A): A
}

trait Multiplication[A] {
  def multiply(e1: A, e2: A): A
}

trait Division[A] {
  def divide(e1: A, e2: A): Option[A]
}