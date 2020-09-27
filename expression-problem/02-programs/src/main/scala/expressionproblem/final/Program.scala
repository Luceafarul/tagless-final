package expressionproblem.initial.`final`

trait Program[A] {
  def run: A
}

object Program {
  def dsl[A](expression: Expression[A]): Program[A] =
    new Program[A] {
      import expression._

      override val run: A = addition(
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
