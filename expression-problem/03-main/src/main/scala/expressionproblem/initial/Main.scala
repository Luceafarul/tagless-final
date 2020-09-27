package expressionproblem.initial

object Main extends App {
  println("─" * 100)

  println(View(Program.exp))
  println(s"Evaluate: ${Eval(Program.exp)}")

  println("─" * 100)

  println(new `final`.Program(`final`.View).repr)
  println(s"Evaluate: ${new `final`.Program(`final`.Eval).repr}")

  println("─" * 100)
}
