package devinsideyou.expressionproblem.initial

sealed trait Expr extends Product with Serializable
object Expr {
  final case class Literal(x: Int) extends Expr
  final case class Negation(e: Expr) extends Expr
  final case class Addition(left: Expr, right: Expr) extends Expr
}
