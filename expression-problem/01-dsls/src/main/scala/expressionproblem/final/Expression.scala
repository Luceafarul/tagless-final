package expressionproblem.initial.`final`

trait Literal[F[_], A] {
  def literal(n: Int): F[A]
}

trait Negation[F[_], A] {
  def negation(e: F[A]): F[A]
}

trait Addition[F[_], A] {
  def addition(e1: F[A], e2: F[A]): F[A]
}

trait Multiplication[F[_], A] {
  def multiply(e1: F[A], e2: F[A]): F[A]
}

trait Division[F[_], A] {
  def divide(e1: F[A], e2: F[A]): F[A]
}
