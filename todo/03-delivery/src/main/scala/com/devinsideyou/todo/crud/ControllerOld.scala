package com.devinsideyou
package todo
package crud

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

trait ControllerOld {
  def run(): Unit
}

object ControllerOld {
  def dsl(
    boundary: BoundaryOld,
    pattern: DateTimeFormatter
    )(implicit
      fancyConsole: FancyConsoleOld,
      random: RandomOld
    ): ControllerOld =
    new ControllerOld {
      override def run(): Unit = {
        val colors: Vector[String] =
          Vector(
            // scala.Console.BLACK,
            scala.Console.BLUE,
            scala.Console.CYAN,
            scala.Console.GREEN,
            scala.Console.MAGENTA,
            scala.Console.RED,
            // scala.Console.WHITE,
            scala.Console.YELLOW
          )

        def randomColor: String =
          random.nextInt(colors.size).pipe(colors)

        def hyphens: String =
          inColor("─" * 100)(randomColor)

        def menu: String =
          s"""|
              |$hyphens
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

        def prompt: String =
          fancyConsole.getStrLnTrimmedWithPrompt(menu)

        @scala.annotation.tailrec
        def loop(shouldKeepLooping: Boolean): Unit =
          if (shouldKeepLooping)
            prompt match {
              case "c"                         => create(); loop(true)
              case "d"                         => delete(); loop(true)
              case "da"                        => deleteAll(); loop(true)
              case "sa"                        => showAll(); loop(true)
              case "sd"                        => searchByPartialDescription(); loop(true)
              case "sid"                       => searchById(); loop(true)
              case "ud"                        => updateDescription(); loop(true)
              case "udl"                       => updateDeadline(); loop(true)
              case "e" | "q" | "exit" | "quit" => exit(); loop(false)
              case _                           => loop(true)
            }

        loop(shouldKeepLooping = true)
      }

      private def descriptionPrompt: String =
        fancyConsole.getStrLnTrimmedWithPrompt("Please enter a description:")

      private def create(): Unit =
        descriptionPrompt.pipe { description =>
          withDeadlinePrompt { deadline =>
            boundary
              .createOne(Todo.Data(description, deadline))
              .tapAs(fancyConsole.putSuccess("Successfully created the new todo."))
          }
        }

      private def deadlinePrompt: String =
        fancyConsole.getStrLnTrimmedWithPrompt(
          s"Please enter a deadline in the following format $DeadlinePromptFormat:"
        )

      private def withDeadlinePrompt(onSuccess: LocalDateTime => Unit): Unit =
        deadlinePrompt.pipe(toLocalDateTime).pipe {
          case Right(deadline) => onSuccess(deadline)
          case Left(error)     => fancyConsole.putError(error)
        }

      private def toLocalDateTime(input: String): Either[String, LocalDateTime] = {
        val formatter = DateTimeFormatter.ofPattern(DeadlinePromptPattern)

        scala
          .util
          .Try(LocalDateTime.parse(input, formatter))
          .toEither
          .left
          .map { _ =>
            s"\n${inColor(input)(scala.Console.YELLOW)} does not match the required format $DeadlinePromptFormat.${scala.Console.RESET}"
          }
      }

      private def delete(): Unit =
        withIdPrompt { id =>
          withReadOne(id) { todo =>
            boundary
              .deleteOne(todo)
              .tapAs(fancyConsole.putSuccess("Successfully deleted the todo."))
          }
        }

      private def idPrompt: String =
        fancyConsole
          .getStrLnTrimmedWithPrompt("Please enter the id:")

      private def withIdPrompt(onValidId: String => Unit): Unit =
        idPrompt.pipe(toId).pipe {
          case Right(id)   => onValidId(id)
          case Left(error) => fancyConsole.putError(error)
        }

      private def toId(userInput: String): Either[String, String] =
        if (userInput.isEmpty || userInput.contains(" "))
          Left(
            s"\n${scala.Console.YELLOW + userInput + scala.Console.RED} is not a valid id.${scala.Console.RESET}"
          )
        else
          Right(userInput)

      private def deleteAll(): Unit =
        boundary
          .deleteAll
          .tapAs(fancyConsole.putSuccess("Successfully deleted all todos."))

      private def withReadOne(id: String)(onFound: Todo.Existing => Unit): Unit =
        boundary
          .readOneById(id)
          .pipe {
            case Some(todo) => onFound(todo)
            case None       => displayNoTodosFoundMessage
          }

      private def showAll(): Unit =
        boundary.readAll.pipe(displayZeroOrMany)

      private def displayZeroOrMany(todos: Vector[Todo.Existing]): Unit =
        if (todos.isEmpty)
          displayNoTodosFoundMessage
        else {
          val uxMatters = if (todos.size == 1) "todo" else "todos"

          val renderedSize: String =
            inColor(todos.size.toString)(scala.Console.GREEN)

          fancyConsole
            .putStrLn(s"\nFound $renderedSize $uxMatters:\n")
            .tapAs {
              todos
                .sortBy(_.deadline)
                .map(renderedWithPattern)
                .foreach(println)
            }
        }

      private def displayNoTodosFoundMessage(): Unit =
        fancyConsole.putWarning("\nNo todos found!")

      private def renderedWithPattern(todo: Todo.Existing): String = {
        def renderedId: String =
          inColor(todo.id.toString)(scala.Console.GREEN)

        def renderedDescription: String =
          inColor(todo.description)(scala.Console.MAGENTA)

        def renderedDeadline: String =
          inColor(todo.deadline.format(pattern))(scala.Console.YELLOW)

        s"$renderedId $renderedDescription is due on $renderedDeadline."
      }

      private def searchByPartialDescription(): Unit =
        descriptionPrompt
          .pipe(boundary.readManyByPartialDescription)
          .pipe(displayZeroOrMany)

      private def searchById(): Unit =
        withIdPrompt { id =>
          boundary
            .readOneById(id)
            .pipe(_.to(Vector))
            .pipe(displayZeroOrMany)
        }

      private def updateDescription(): Unit =
        withIdPrompt { id =>
          withReadOne(id) { todo =>
            descriptionPrompt.pipe { description =>
              boundary
                .updateOne(todo.withUpdatedDescription(description))
                .tapAs(fancyConsole.putSuccess("Successfully updated the description."))
            }
          }
        }

      private def updateDeadline(): Unit =
        withIdPrompt { id =>
          withReadOne(id) { todo =>
            withDeadlinePrompt { deadline =>
              boundary
                .updateOne(todo.withUpdatedDeadline(deadline))
                .tapAs(fancyConsole.putSuccess("Successfully updated the deadline."))
            }
          }
        }

      private def exit(): Unit =
        println("\nUntil next time!\n")
    }

  private def DeadlinePromptPattern(): String =
    "yyyy-M-d H:m"

  private def DeadlinePromptFormat(): String =
    inColor(DeadlinePromptPattern)(scala.Console.MAGENTA)

  private def inColor(line: String)(color: String): String =
    color + line + scala.Console.RESET
}
