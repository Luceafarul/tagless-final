package expressionproblem.initial.`final`

object Evaluate {
  object Expression {
    val dsl: Expression[Int] = new Expression[Int] {
      override def literal(n: Int): Int = n
      override def negation(e: Int): Int = -e
      override def addition(e1: Int, e2: Int): Int = e1 + e2
    }
  }

  object Multiplication {
    val dsl: Multiplication[Int] = new Multiplication[Int] {
      override def multiply(e1: Int, e2: Int): Int = e1 * e2
    }
  }

  object Division {
    val dsl: Division[Int] = new Division[Int] {
      override def divide(e1: Int, e2: Int): Option[Int] =
        if (e2 != 0) Some(e1 / e2) else None
    }
  }
}
