package expressionproblem.initial.`final`

object View {
  val dsl: Expression[String] = new Expression[String] {
    override def literal(n: Int): String = s"$n"
    override def negation(e: String): String = s"(-$e)"
    override def addition(e1: String, e2: String): String = s"($e1 + $e2)"
  }
}
