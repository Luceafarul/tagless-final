package devinsideyou.expressionproblem.`final`

trait Literal[F[_], A] {
  def literal(x: Int): F[A]
}

trait Negation[F[_], A] {
  def negation(x: F[A]): F[A]
}

trait Addition[F[_], A] {
  def addition(left: F[A], right: F[A]): F[A]
}

trait Multiplication[F[_], A] {
  def multiply(left: F[A], right: F[A]): F[A]
}

trait Division[F[_], A] {
  def divide(left: F[A], right: F[A]): F[A]
}
