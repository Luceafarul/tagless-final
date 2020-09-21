package expressionproblem.initial

sealed abstract class Exp extends Product with Serializable
object Exp {
  final case class Literal(n: Int) extends Exp
  final case class Negation(e: Exp) extends Exp
  final case class Addition(e1: Exp, e2: Exp) extends Exp
}
