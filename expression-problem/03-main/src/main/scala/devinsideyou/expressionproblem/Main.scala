package devinsideyou
package expressionproblem

object Main extends App {
  println("─" * 100)

  println(initial.Eval(initial.Program.exp))
  println(initial.View(initial.Program.exp))
  println(initial.View.prefix(initial.Program.exp))

  println("─" * 100)
}
