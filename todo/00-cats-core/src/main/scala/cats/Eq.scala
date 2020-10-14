package cats

trait Eq[A] {
  def eqv(x: A, y: A): Boolean

  def neqv(x: A, y: A): Boolean = !eqv(x, y)
}

object Eq {
  def apply[A: Eq]: Eq[A] = implicitly[Eq[A]]
}
