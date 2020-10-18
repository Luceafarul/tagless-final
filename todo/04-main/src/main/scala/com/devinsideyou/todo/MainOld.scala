package com.devinsideyou.todo

import java.time.format.DateTimeFormatter

object MainOld {
  val crudController: crud.ControllerOld =
    crud
      .DependencyGraphOld
      .dsl(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy HH:mm"))

  crudController.run()
}
