package devinsideyou
package expressionproblem

object Main extends App {
  println("─" * 100)

  println(initial.Eval(initial.Program.exp))
  println(initial.View(initial.Program.exp))

  println("─" * 100)

  import `final`._

  println(new Program(Eval).repr)
  println(new Program(View).repr)

  println("─" * 100)
}
