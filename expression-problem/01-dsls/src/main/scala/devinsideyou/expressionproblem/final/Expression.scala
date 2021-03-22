package devinsideyou.expressionproblem.`final`

trait Expression[A] {
  def literal(x: Int): Option[A]
  def negation(x: Option[A]): Option[A]
  def addition(left: Option[A], right: Option[A]): Option[A]
}

trait Multiplication[A] {
  def multiply(left: Option[A], right: Option[A]): Option[A]
}

trait Division[A] {
  def divide(left: Option[A], right: Option[A]): Option[A]
}
