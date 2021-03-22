package devinsideyou.expressionproblem.`final`

object View {
  object Expression {
    val dsl: Expression[String] = new Expression[String] {
      def literal(x: Int): Option[String] = Some(s"$x")
      def negation(x: Option[String]): Option[String] = x.map(x => s"(-$x)")
      def addition(
          left: Option[String],
          right: Option[String]
        ): Option[String] =
        left.zip(right).map { case (left, right) => s"($left + $right)" }
    }
  }

  object Multiplication {
    val dsl: Multiplication[String] = new Multiplication[String] {
      def multiply(
          left: Option[String],
          right: Option[String]
        ): Option[String] =
        left.zip(right).map { case (left, right) => s"($left * $right)" }
    }
  }

  object Division {
    val dsl: Division[String] = new Division[String] {
      def divide(left: Option[String], right: Option[String]): Option[String] =
        for {
          l <- left
          r <- right
        } yield s"($l / $r)"
    }
  }
}
