package com.devinsideyou.todo

import java.time.format.DateTimeFormatter

import cats.effect.Sync

object Program {
  def dsl[F[_] : Sync]: F[Unit] = {
    val crudController: crud.Controller[F] =
      crud
        .DependencyGraph
        .dsl(pattern, Random.dsl, Console.dsl)

    val program: F[Unit] = crudController.program

    println(s"[${scala.Console.YELLOW}warn${scala.Console.RESET}] Any output before this line is a bug!")

    program
  }

  private val pattern = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy HH:mm")
}
