package com.devinsideyou.todo

import java.time.format.DateTimeFormatter

import cats.effect.Sync
import cats.implicits._
import com.devinsideyou.todo.Program.pattern
import com.devinsideyou.todo.crud.DependencyGraph

object Program {
  def dsl[F[_] : Sync]: F[Unit] = {
    val program: F[Unit] =
      for {
        console <- Console.dsl
        random <- Random.dsl
        controller <- crud.DependencyGraph.dsl(pattern, random, console)
        _ <- controller.program
      } yield ()

    println(s"[${scala.Console.YELLOW}warn${scala.Console.RESET}] Any output before this line is a bug!")

    program
  }

  private val pattern = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy HH:mm")
}
