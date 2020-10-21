package com.devinsideyou.todo.crud

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import cats._
import cats.implicits._
import com.devinsideyou.Todo
import com.devinsideyou.todo.{FancyConsole, Random}

trait Controller[F[_]] {
  def program: F[Unit]
}

object Controller {
  def dsl[F[_] : Monad](
      boundary: Boundary[F],
      random: Random[F],
      console: FancyConsole[F],
      pattern: DateTimeFormatter
    ): Controller[F] =
    new Controller[F] {
      override val program: F[Unit] = {
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
          random.nextInt(colors.size).map(colors)

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
          menu.flatMap(console.getStrLnTrimmedWithPrompt)

        prompt.flatMap {
          case "c" => create.as(true)
          case "d" => delete.as(true)
          case "da" => deleteAll.as(true)
          case "sa" => showAll.as(true)
          case "sd" => searchByPartialDescription.as(true)
          case "sid" => searchById.as(true)
          case "ud" => updateDescription.as(true)
          case "udl" => updateDeadline.as(true)
          case "e" | "q" | "exit" | "quit" => exit.as(false)
          case _ => true.pure[F]
        }.iterateWhile(identity).void
      }

      private val create: F[Unit] = descriptionPrompt.flatMap { description =>
        withDeadlinePrompt { deadline =>
          boundary.createOne(Todo.Data(description, deadline)) >>
            console.putSuccess("Successfully created the new todo.")
        }
      }

      private val delete: F[Unit] = withIdPrompt { id =>
        withReadOne(id) { todo =>
          boundary.deleteOne(todo) >> console.putSuccess("Successfully deleted the todo.")
        }
      }

      private val deleteAll: F[Unit] =
        boundary.deleteAll >> console.putSuccess("Successfully deleted all todos.")

      private val showAll: F[Unit] =
        boundary.readAll.flatMap(displayZeroOrMany)

      private val searchByPartialDescription: F[Unit] =
        descriptionPrompt
          .flatMap(boundary.readManyByPartialDescription)
          .flatMap(displayZeroOrMany)

      private val searchById: F[Unit] = withIdPrompt { id =>
        boundary
          .readOneById(id)
          .map(_.to(Vector))
          .flatMap(displayZeroOrMany)
      }

      private val updateDescription: F[Unit] = withIdPrompt { id =>
        withReadOne(id) { todo =>
          descriptionPrompt.flatMap { description =>
            boundary.updateOne(todo.withUpdatedDescription(description)) >>
              console.putSuccess("Successfully updated the description.")
          }
        }
      }

      private val updateDeadline: F[Unit] = withIdPrompt { id =>
        withReadOne(id) { todo =>
          withDeadlinePrompt { deadline =>
            boundary.updateOne(todo.withUpdatedDeadline(deadline)) >>
              console.putSuccess("Successfully updated the deadline.")
          }
        }
      }

      private val exit: F[Unit] =
        console.putStrLn("\nUntil next time!\n")

      private def descriptionPrompt: F[String] =
        console.getStrLnTrimmedWithPrompt("Please enter a description:")

      private def withDeadlinePrompt(onSuccess: LocalDateTime => F[Unit]): F[Unit] =
        deadlinePrompt.map(toLocalDateTime).flatMap {
          case Right(deadline) => onSuccess(deadline)
          case Left(error) => console.putError(error)
        }

      private def deadlinePrompt: F[String] =
        console.getStrLnTrimmedWithPrompt(
          s"Please enter a deadline in the following format ${DeadlinePromptFormat()}:"
        )

      private def DeadlinePromptFormat(): String =
        inColor(DeadlinePromptPattern())(scala.Console.MAGENTA)

      private def DeadlinePromptPattern(): String =
        "yyyy-M-d H:m"

      private def toLocalDateTime(input: String): Either[String, LocalDateTime] = {
        val formatter = DateTimeFormatter.ofPattern(DeadlinePromptPattern())

        scala
          .util
          .Try(LocalDateTime.parse(input, formatter))
          .toEither
          .left
          .map { _ =>
            s"\n${inColor(input)(scala.Console.YELLOW)} does not match the required format ${DeadlinePromptFormat()}.${scala.Console.RESET}"
          }
      }

      private def idPrompt: F[String] =
        console.getStrLnTrimmedWithPrompt("Please enter the id:")

      private def withIdPrompt(onValidId: String => F[Unit]): F[Unit] =
        idPrompt.map(toId).flatMap {
          case Right(id) => onValidId(id)
          case Left(error) => console.putError(error)
        }

      private def toId(userInput: String): Either[String, String] =
        if (userInput.isEmpty || userInput.contains(" "))
          Left(s"\n${scala.Console.YELLOW + userInput + scala.Console.RED} is not a valid id.${scala.Console.RESET}")
        else
          Right(userInput)

      private def withReadOne(id: String)(onFound: Todo.Existing => F[Unit]): F[Unit] =
        boundary
          .readOneById(id)
          .flatMap {
            case Some(todo) => onFound(todo)
            case None => displayNoTodosFoundMessage()
          }

      private def displayNoTodosFoundMessage(): F[Unit] =
        console.putWarning("\nNo todos found!")

      private def displayZeroOrMany(todos: Vector[Todo.Existing]): F[Unit] =
        if (todos.isEmpty) displayNoTodosFoundMessage()
        else {
          val uxMatters = if (todos.size == 1) "todo" else "todos"

          val renderedSize: String =
            inColor(todos.size.toString)(scala.Console.GREEN)

          console.putStrLn(s"\nFound $renderedSize $uxMatters:\n") >> {
            todos
              .sortBy(_.deadline)
              .map(renderedWithPattern)
              .traverse(console.putStrLn)
              .void
          }
        }

      private def renderedWithPattern(todo: Todo.Existing): String = {
        def renderedId: String =
          inColor(todo.id)(scala.Console.GREEN)

        def renderedDescription: String =
          inColor(todo.description)(scala.Console.MAGENTA)

        def renderedDeadline: String =
          inColor(todo.deadline.format(pattern))(scala.Console.YELLOW)

        s"$renderedId $renderedDescription is due on $renderedDeadline."
      }

    }

  private def inColor(line: String)(color: String): String =
    color + line + scala.Console.RESET
}
