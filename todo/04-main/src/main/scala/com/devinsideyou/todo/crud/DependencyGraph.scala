package com.devinsideyou.todo.crud

import java.time.format.DateTimeFormatter

import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.implicits._
import com.devinsideyou.Todo
import com.devinsideyou.todo.{Console, FancyConsole, Random}

object DependencyGraph {
  def dsl[F[_] : Sync](
    pattern: DateTimeFormatter,
    random: Random[F],
    console: Console[F]
  ): F[Controller[F]] =
    Ref.of(Vector.empty[Todo.Existing]).map { state =>
      Controller.dsl(
        pattern = pattern,
        random = random,
        console = FancyConsole.dsl(console),
        boundary = Boundary.dsl(gateway = InMemoryEntityGateway.dsl(state))
      )
    }
}
