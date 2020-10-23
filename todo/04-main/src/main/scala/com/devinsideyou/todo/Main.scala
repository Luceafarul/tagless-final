package com.devinsideyou.todo

object Main extends App {
  scala.util.Random.nextInt(3) match {
    case 0 =>
      println(inColor("Running on cats.effect.IO")(scala.Console.GREEN))

      Program.dsl[cats.effect.IO].unsafeRunSync()

    case 1 =>
      println(inColor("Running on monix.eval.Task")(scala.Console.BLUE))

      import monix.execution.Scheduler.Implicits.global

      Program.dsl[monix.eval.Task].runSyncUnsafe()

    case _ =>
      println(inColor("Running on zio.Task")(scala.Console.RED))

      import zio.interop.catz._

      zio.Runtime.default.unsafeRunSync(Program.dsl[zio.Task])
  }

  private def inColor(text: String)(color: String): String =
    color + text + scala.Console.RESET
}
