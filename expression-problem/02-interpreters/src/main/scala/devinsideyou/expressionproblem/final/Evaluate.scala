package devinsideyou.expressionproblem.`final`

object Evaluate {
  object Expression {
    val dsl: Expression[Option, Int] = new Expression[Option, Int] {
      def literal(x: Int): Option[Int] = Some(x)
      def negation(x: Option[Int]): Option[Int] = x.map(x => -x)
      def addition(left: Option[Int], right: Option[Int]): Option[Int] =
        left.zip(right).map {
          case (left, right) => left + right
        }
    }
  }

  object Multiplication {
    val dsl: Multiplication[Option, Int] = new Multiplication[Option, Int] {
      def multiply(left: Option[Int], right: Option[Int]): Option[Int] =
        left.zip(right).map {
          case (left, right) => left * right
        }
    }
  }

  object Division {
    val dsl: Division[Option, Int] = new Division[Option, Int] {
      def divide(left: Option[Int], right: Option[Int]): Option[Int] =
        left.zip(right).flatMap {
          case (_, 0)        => None
          case (left, right) => Some(left / right)
        }
    }
  }
}
