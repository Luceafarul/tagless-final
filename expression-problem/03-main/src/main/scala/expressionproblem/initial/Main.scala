package expressionproblem.initial

object Main extends App {
  println("─" * 100)

  println(View(Program.exp))
  println(s"Evaluate: ${Eval(Program.exp)}")

  println("─" * 100)

  import `final`._

  println(Program.dsl(View.dsl).run)
  println(s"Evaluate: ${Program.dsl(Evaluate.dsl).run}")

  println("─" * 100)
}
