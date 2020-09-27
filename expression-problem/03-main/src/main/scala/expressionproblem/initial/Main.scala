package expressionproblem.initial

object Main extends App {
  println("─" * 100)

  println(View(Program.exp))
  println(s"Evaluate: ${Eval(Program.exp)}")

  println("─" * 100)

  import  `final`._

  println(Program.dsl(View.dsl).repr)
  println(s"Evaluate: ${Program.dsl(Eval.dsl).repr}")

  println("─" * 100)
}
