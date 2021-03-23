package devinsideyou.expressionproblem.`final`

trait Expression[F[_], A] {
  def literal(x: Int): F[A]
  def negation(x: F[A]): F[A]
  def addition(left: F[A], right: F[A]): F[A]
}

trait Multiplication[F[_], A] {
  def multiply(left: F[A], right: F[A]): F[A]
}

trait Division[F[_], A] {
  def divide(left: F[A], right: F[A]): F[A]
}
