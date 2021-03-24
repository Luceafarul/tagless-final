package devinsideyou.expressionproblem

import scala.util.chaining._

import cats._
import cats.instances.all._

object Main extends App {
  println("─" * 100)

  println(initial.Eval.interpreter(initial.Program.exp))
  println(initial.View.interpreter(initial.Program.exp))

  println("─" * 100)

  import `final`._

  Program
    .Expression
    .dsl(
      Evaluate.Literal.dsl[Id],
      Evaluate.Negation.dsl[Id],
      Evaluate.Addition.dsl[Id]
    )
    .run
    .tap(println)

  Program
    .Expression
    .dsl(
      View.Literal.dsl[Id],
      View.Negation.dsl[Id],
      View.Addition.dsl[Id]
    )
    .run
    .tap(println)

  println("─" * 100)

  Program
    .Multiplication
    .dsl[Id, Int](
      Evaluate.Literal.dsl,
      Evaluate.Negation.dsl,
      Evaluate.Addition.dsl,
      Evaluate.Multiplication.dsl
    )
    .run
    .tap(println)

  Program
    .Multiplication
    .dsl[Id, String](
      View.Literal.dsl,
      View.Negation.dsl,
      View.Addition.dsl,
      View.Multiplication.dsl
    )
    .run
    .tap(println)

  println("─" * 100)

  Program
    .MultiplicationInTheMiddle
    .dsl[Id, Int](
      Evaluate.Literal.dsl,
      Evaluate.Negation.dsl,
      Evaluate.Addition.dsl,
      Evaluate.Multiplication.dsl
    )
    .run
    .tap(println)

  Program
    .MultiplicationInTheMiddle
    .dsl[Id, String](
      View.Literal.dsl,
      View.Negation.dsl,
      View.Addition.dsl,
      View.Multiplication.dsl
    )
    .run
    .tap(println)

  println("─" * 100)

  Program
    .Division
    .dsl[Either[String, *], Int](
      Evaluate.Literal.dsl,
      Evaluate.Negation.dsl,
      Evaluate.Addition.dsl,
      Evaluate.Multiplication.dsl,
      Evaluate.Division.dsl
    )
    .run
    .tap(println)

  Program
    .Division
    .dsl[Id, String](
      View.Literal.dsl,
      View.Negation.dsl,
      View.Addition.dsl,
      View.Multiplication.dsl,
      View.Division.dsl
    )
    .run
    .tap(println)

  println("─" * 100)

  Program
    .DivisionInTheMiddle
    .dsl[Either[String, *], Int](
      Evaluate.Literal.dsl,
      Evaluate.Negation.dsl,
      Evaluate.Addition.dsl,
      Evaluate.Multiplication.dsl,
      Evaluate.Division.dsl
    )
    .run
    .tap(println)

  Program
    .DivisionInTheMiddle
    .dsl[Id, String](
      View.Literal.dsl,
      View.Negation.dsl,
      View.Addition.dsl,
      View.Multiplication.dsl,
      View.Division.dsl
    )
    .run
    .tap(println)

  println("─" * 100)
}
