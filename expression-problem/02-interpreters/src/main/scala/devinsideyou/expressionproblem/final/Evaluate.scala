package devinsideyou.expressionproblem.`final`

object Evaluate {
  object Expression {
    val dsl: Expression[Int] = new Expression[Int] {
      def literal(x: Int): Option[Int] = Some(x)
      def negation(x: Option[Int]): Option[Int] = x.map(x => -x)
      def addition(left: Option[Int], right: Option[Int]): Option[Int] =
        left.zip(right).map {
          case (left, right) => left + right
        }
    }
  }

  object Multiplication {
    val dsl: Multiplication[Int] = new Multiplication[Int] {
      def multiply(left: Option[Int], right: Option[Int]): Option[Int] =
        left.zip(right).map {
          case (left, right) => left * right
        }
    }
  }

  object Division {
    val dsl: Division[Int] = new Division[Int] {
      def divide(left: Option[Int], right: Option[Int]): Option[Int] =
        left.zip(right).flatMap {
          case (_, 0)        => None
          case (left, right) => Some(left / right)
        }
    }
  }
}
