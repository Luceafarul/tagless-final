package com.devinsideyou.todo.crud

import java.time.format.DateTimeFormatter

import cats.effect.Sync
import com.devinsideyou.todo.{Console, FancyConsole, Random}

object DependencyGraph {
  def dsl[F[_] : Sync](pattern: DateTimeFormatter, random: Random[F], console: Console[F]): Controller[F] =
    Controller.dsl(
      pattern = pattern,
      random = random,
      console = FancyConsole.dsl(console),
      boundary = Boundary.dsl(gateway = InMemoryEntityGateway.dsl)
    )
}
