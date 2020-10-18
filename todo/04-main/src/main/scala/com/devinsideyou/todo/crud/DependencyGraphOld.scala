package com.devinsideyou.todo.crud

import java.time.format.DateTimeFormatter

import com.devinsideyou.todo.{FancyConsoleOld, RandomOld}

object DependencyGraphOld {
  def dsl(
      pattern: DateTimeFormatter
    )(implicit
      fancyConsole: FancyConsoleOld,
      random: RandomOld
    ): ControllerOld =
    ControllerOld.dsl(
      pattern = pattern,
      boundary = BoundaryOld.dsl(
        gateway = InMemoryEntityGatewayOld.dsl
      )
    )
}
