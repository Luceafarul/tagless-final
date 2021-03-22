package devinsideyou.expressionproblem.`final`

trait Program[A] {
  def repr: A
}

object Program {
  def dsl[A](expr: Expr[A]): Program[A] =
    new Program[A] {
      import expr._

      val repr: A = addition(
        literal(16),
        negation(
          addition(
            literal(1),
            literal(2)
          )
        )
      )
    }
}
