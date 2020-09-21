package expressionproblem.initial

object Main extends App {
  println("─" * 100)

  println(View(Program.exp))

  println("─" * 100)

  println(s"Evaluate: ${Eval(Program.exp)}")

  println("─" * 100)
}
