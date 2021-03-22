package devinsideyou.expressionproblem.`final`

object View {
  object Expression {
    val dsl: Expression[String] = new Expression[String] {
      def literal(x: Int): String = s"$x"
      def negation(x: String): String = s"(-$x)"
      def addition(left: String, right: String): String = s"($left + $right)"
    }
  }

  object Multiplication {
    val dsl: Multiplication[String] = new Multiplication[String] {
      def multiply(left: String, right: String): String = s"($left * $right)"
    }
  }
}
