package expressionproblem.initial.`final`

trait Expression[A] {
  def literal(n: Int): Option[A]
  def negation(e: Option[A]): Option[A]
  def addition(e1: Option[A], e2: Option[A]): Option[A]
}

trait Multiplication[A] {
  def multiply(e1: Option[A], e2: Option[A]): Option[A]
}

trait Division[A] {
  def divide(e1: Option[A], e2: Option[A]): Option[A]
}
