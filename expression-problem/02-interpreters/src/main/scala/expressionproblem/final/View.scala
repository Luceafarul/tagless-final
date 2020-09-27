package expressionproblem.initial.`final`

object View {
  object Expression {
    val dsl: Expression[String] = new Expression[String] {
      override def literal(n: Int): Option[String] = Some(s"$n")
      override def negation(e: Option[String]): Option[String] =
        e.map(e => s"(-$e)")
      override def addition(
          e1: Option[String],
          e2: Option[String]
        ): Option[String] = e1.zip(e2).map { case (e1, e2) => s"($e1 + $e2)" }
    }
  }

  object Multiplication {
    val dsl: Multiplication[String] = new Multiplication[String] {
      override def multiply(
          e1: Option[String],
          e2: Option[String]
        ): Option[String] = e1.zip(e2).map { case (e1, e2) => s"($e1 * $e2)" }
    }
  }

  object Division {
    val dsl: Division[String] = new Division[String] {
      override def divide(
          e1: Option[String],
          e2: Option[String]
        ): Option[String] = e1.zip(e2).map { case (e1, e2) => s"($e1 / $e2)" }
    }
  }
}
