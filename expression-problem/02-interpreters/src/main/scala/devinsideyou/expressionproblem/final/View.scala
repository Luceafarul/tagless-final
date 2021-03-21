package devinsideyou.expressionproblem.`final`

object View extends Expr[String] {
  def literal(x: Int): String = s"$x"
  def negation(x: String): String = s"(-$x)"
  def addition(left: String, right: String): String = s"($left + $right)"
}
