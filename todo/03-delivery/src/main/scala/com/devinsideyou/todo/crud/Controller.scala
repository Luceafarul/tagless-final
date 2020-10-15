package com.devinsideyou.todo.crud

import java.time.format.DateTimeFormatter

import cats._
import cats.implicits._
import com.devinsideyou.todo.{FancyConsole, Random}

trait Controller[F[_]] {
  def run: F[Unit]
}

object Controller {
  def dsl[F[_] : Random : FancyConsole : FlatMap : Functor](boundary: Boundary[F], pattern: DateTimeFormatter): Controller[F] =
    new Controller[F] {
      override val run: F[Unit] = {
        val colors: Vector[String] =
          Vector(
            scala.Console.BLUE,
            scala.Console.CYAN,
            scala.Console.GREEN,
            scala.Console.MAGENTA,
            scala.Console.RED,
            scala.Console.YELLOW
          )

        def randomColor: F[String] =
          Random[F].nextInt(colors.size).map(colors)

        def hyphens: F[String] =
          randomColor.map(color => inColor("─" * 100)(color))

        def menu: F[String] = hyphens.map { h =>
          s"""|
              |$h
              |
              |c                   => create new todo
              |d                   => delete todo
              |da                  => delete all todos
              |sa                  => show all todos
              |sd                  => search by partial description
              |sid                 => search by id
              |ud                  => update description
              |udl                 => update deadline
              |e | q | exit | quit => exit the application
              |anything else       => show the main menu
              |
              |Please enter a command:""".stripMargin
        }

        def prompt: F[String] =
          menu.flatMap(FancyConsole[F].getStrLnTrimmedWithPrompt)

        ???
      }
    }

  private def inColor(line: String)(color: String): String =
    color + line + scala.Console.RESET
}
