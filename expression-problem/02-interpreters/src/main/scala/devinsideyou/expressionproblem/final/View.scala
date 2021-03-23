package devinsideyou.expressionproblem.`final`

object View {
  object Expression {
    val dsl: Expression[Option, String] = new Expression[Option, String] {
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
    val dsl: Multiplication[Option, String] = new Multiplication[Option, String] {
      def multiply(
          left: Option[String],
          right: Option[String]
        ): Option[String] =
        left.zip(right).map { case (left, right) => s"($left * $right)" }
    }
  }

  object Division {
    val dsl: Division[Option, String] = new Division[Option, String] {
      def divide(left: Option[String], right: Option[String]): Option[String] =
        for {
          l <- left
          r <- right
        } yield s"($l / $r)"
    }
  }
}
