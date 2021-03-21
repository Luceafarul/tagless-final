package devinsideyou.expressionproblem.`final`

object Eval {
  val dsl: Expr[Int] = new Expr[Int] {
    def literal(x: Int): Int = x
    def negation(x: Int): Int = -x
    def addition(left: Int, right: Int): Int = left + right
  }
}
