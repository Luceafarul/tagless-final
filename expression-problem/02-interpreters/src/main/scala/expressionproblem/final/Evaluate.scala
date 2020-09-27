package expressionproblem.initial.`final`

object Evaluate {
  object Expression {
    val dsl: Expression[Int] = new Expression[Int] {
      override def literal(n: Int): Option[Int] = Some(n)
      override def negation(e: Option[Int]): Option[Int] = e.map(-_)
      override def addition(e1: Option[Int], e2: Option[Int]): Option[Int] =
        e1.zip(e2).map { case (e1, e2) => e1 + e2 }
    }
  }

  object Multiplication {
    val dsl: Multiplication[Int] = new Multiplication[Int] {
      override def multiply(e1: Option[Int], e2: Option[Int]): Option[Int] =
        e1.zip(e2).map { case (e1, e2) => e1 * e2 }
    }
  }

  object Division {
    val dsl: Division[Int] = new Division[Int] {
      override def divide(e1: Option[Int], e2: Option[Int]): Option[Int] =
        e1.zip(e2).flatMap {
          case (e1, 0)  => None
          case (e1, e2) => Some(e1 / e2)
        }
    }
  }
}
