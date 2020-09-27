package expressionproblem.initial.`final`

object Evaluate {
  val dsl: Expression[Int] = new Expression[Int] {
    override def literal(n: Int): Int = n
    override def negation(e: Int): Int = -e
    override def addition(e1: Int, e2: Int): Int = e1 + e2
  }
}
