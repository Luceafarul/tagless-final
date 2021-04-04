package com.devinsideyou.todo

import com.devinsideyou.todo.crud.{Controller, DependencyGraph}
import handmade.cats.effect.Sync

import java.time.format.DateTimeFormatter

object Program {
  private val pattern = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy HH:mm")

  def dsl[F[_]: Sync]: F[Unit] = {
    val crudController: Controller[F] =
      new DependencyGraph[F]
        .dsl(pattern, Console.dsl, Random.dsl)

    val program: F[Unit] = crudController.program

    println(
      s"[${scala.Console.YELLOW}warn${scala.Console.RESET}] Any output before this line is a bug!"
    )

    program
  }
}
