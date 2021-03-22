package devinsideyou
package expressionproblem

object Main extends App {
  println("─" * 100)

  println(initial.Eval.interpreter(initial.Program.exp))
  println(initial.View.interpreter(initial.Program.exp))

  println("─" * 100)

  import `final`._

  println(Program.Expression.dsl(Evaluate.Expression.dsl).run)
  println(Program.Expression.dsl(View.Expression.dsl).run)

  println("─" * 100)

  println(
    Program
      .Multiplication
      .dsl(Evaluate.Expression.dsl, Evaluate.Multiplication.dsl)
      .run
  )
  println(
    Program
      .Multiplication
      .dsl(View.Expression.dsl, View.Multiplication.dsl)
      .run
  )

  println("─" * 100)

    println(
    Program
      .Multiplication
      .dsl(Evaluate.Expression.dsl, Evaluate.Multiplication.dsl)
      .run
  )
  println(
    Program
      .Multiplication
      .dsl(View.Expression.dsl, View.Multiplication.dsl)
      .run
  )

  println("─" * 100)

      println(
    Program
      .MultiplicationInTheMiddle
      .dsl(Evaluate.Expression.dsl, Evaluate.Multiplication.dsl)
      .run
  )
  println(
    Program
      .MultiplicationInTheMiddle
      .dsl(View.Expression.dsl, View.Multiplication.dsl)
      .run
  )

  println("─" * 100)
}
