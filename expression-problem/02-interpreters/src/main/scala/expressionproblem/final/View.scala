package expressionproblem.initial.`final`

object View {
  object Expression {
    val dsl: Expression[String] = new Expression[String] {
      override def literal(n: Int): String = s"$n"
      override def negation(e: String): String = s"(-$e)"
      override def addition(e1: String, e2: String): String = s"($e1 + $e2)"
    }
  }

  object Multiplication {
    val dsl: Multiplication[String] = new Multiplication[String] {
      override def multiply(e1: String, e2: String): String = s"($e1 * $e2)"
    }
  }

  object Division {
    val dsl: Division[String] = new Division[String] {
      override def divide(e1: String, e2: String): Option[String] =
        Some(s"($e1 * $e2)")
    }
  }
}
