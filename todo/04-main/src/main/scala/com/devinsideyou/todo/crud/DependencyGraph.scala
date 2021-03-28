package com.devinsideyou.todo.crud

import com.devinsideyou.todo.{Console, FancyConsole, Random}
import handmade.cats.effect.Sync

import java.time.format.DateTimeFormatter

class DependencyGraph[F[_]: Sync] {
  def dsl(
    pattern: DateTimeFormatter,
    console: Console[F],
    random: Random[F]
  ): Controller[F] =
    Controller.dsl(
      pattern = pattern,
      boundary = Boundary.dsl(
        gateway = InMemoryEntityGateway.dsl
      ),
      fancyConsole = FancyConsole.dsl(console),
      random = random
    )
}
