package com.devinsideyou.todo.crud

import handmade.cats.effect.Sync

import java.time.format.DateTimeFormatter

class DependencyGraph[F[_]: Sync] {
  def dsl(pattern: DateTimeFormatter): Controller[F] =
    Controller.dsl(
      pattern = pattern,
      boundary = Boundary.dsl(
        gateway = InMemoryEntityGateway.dsl
      )
    )
}
