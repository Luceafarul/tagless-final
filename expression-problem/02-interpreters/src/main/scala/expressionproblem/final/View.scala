package expressionproblem.initial.`final`

object View extends Exp[String] {
  def literal(n: Int): String = s"$n"
  def negation(e: String): String = s"(-$e)"
  def addition(e1: String, e2: String): String = s"($e1 + $e2)"
}
