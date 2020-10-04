package expressionproblem.initial

import scala.util.chaining._
import cats._
import cats.instances.all._

object Main extends App {
  println("─" * 100)

  import `final`._

  Program
    .Expression
    .dsl[Id, String](View.Expression.dsl)
    .run
    .tap(println)

  Program
    .Expression
    .dsl[Id, Int](Evaluate.Expression.dsl)
    .run
    .tap(res => println(s"Evaluate: $res"))

  println("─" * 100)

  Program
    .Multiplication
    .dsl[Id, String](View.Expression.dsl, View.Multiplication.dsl)
    .run
    .tap(println)

  Program
    .Multiplication
    .dsl[Id, Int](Evaluate.Expression.dsl, Evaluate.Multiplication.dsl)
    .run
    .tap(res => println(s"Evaluate: $res"))

  println("─" * 100)

  Program
    .MultiplicationInTheMiddle
    .dsl[Id, String](View.Expression.dsl, View.Multiplication.dsl)
    .run
    .tap(println)

  Program
    .MultiplicationInTheMiddle
    .dsl[Id, Int](Evaluate.Expression.dsl, Evaluate.Multiplication.dsl)
    .run
    .tap(res => println(s"Evaluate: $res"))

  println("─" * 100)

  Program
    .Division
    .dsl[Id, String](View.Expression.dsl, View.Multiplication.dsl, View.Division.dsl)
    .run
    .tap(println)

  Program
    .Division
    .dsl[Either[String, *], Int](
      Evaluate.Expression.dsl,
      Evaluate.Multiplication.dsl,
      Evaluate.Division.dsl
    )
    .run
    .tap(res => println(s"Evaluate: $res"))

  println("─" * 100)

  Program
    .DivisionInTheMiddle
    .dsl[Id, String](View.Expression.dsl, View.Multiplication.dsl, View.Division.dsl)
    .run
    .tap(println)

  Program
    .DivisionInTheMiddle
    .dsl[Either[String, *], Int](
      Evaluate.Expression.dsl,
      Evaluate.Multiplication.dsl,
      Evaluate.Division.dsl
    )
    .run
    .tap(res => println(s"Evaluate: $res"))

  println("─" * 100)
}
