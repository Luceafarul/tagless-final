package devinsideyou.expressionproblem.`final`

object Evaluate {
  object Expression {
    val dsl: Expression[Int] = new Expression[Int] {
      def literal(x: Int): Int = x
      def negation(x: Int): Int = -x
      def addition(left: Int, right: Int): Int = left + right
    }
  }

  object Multiplication {
    val dsl: Multiplication[Int] = new Multiplication[Int] {
      def multiply(left: Int, right: Int): Int = left * right
    }
  }

    object Division {
    val dsl: Division[Int] = new Division[Int] {
      def divide(left: Int, right: Int): Option[Int] = 
        if (right == 0) None else Some(left / right)
    }
  }
}
