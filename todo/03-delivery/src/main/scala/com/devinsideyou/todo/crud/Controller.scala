package com.devinsideyou.todo.crud

import com.devinsideyou.Todo
import com.devinsideyou.todo.{FancyConsole, Random}
import handmade.cats._
import handmade.cats.core.Functor
import handmade.cats.core.implicits._

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

trait Controller[F[_]] {
  def run(): F[Unit]
}

object Controller {
  def dsl[F[_]: FancyConsole : Random : Functor : Monad](
    boundary: Boundary[F],
    pattern: DateTimeFormatter
  ): Controller[F] = () => {
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
      randomColor.map(inColor("─" * 100))

    val menu: F[String] = hyphens.map { h =>
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
      menu.flatMap { m => FancyConsole[F].getStrLnTrimmedWithPrompt(m) }

    prompt.flatMap {
      case "c"                         => create().as(true)
      case "d"                         => delete().as(true)
      case "da"                        => deleteAll().as(true)
      case "sa"                        => showAll().as(true)
      case "sd"                        => searchByPartialDescription().as(true)
      case "sid"                       => searchById().as(true)
      case "ud"                        => updateDescription().as(true)
      case "udl"                       => updateDeadline().as(true)
      case "e" | "q" | "exit" | "quit" => exit().as(false)
      case _                           => true.pure[F]
    }.iterateWhile(identity).void

    def descriptionPrompt: F[String] =
      FancyConsole[F].getStrLnTrimmedWithPrompt("Please enter a description:")

    def create(): F[Unit] =
      descriptionPrompt.map { description =>
        withDeadlinePrompt { deadline =>
          boundary
            .createOne(Todo.Data(description, deadline))
            .as(FancyConsole[F].putSuccess("Successfully created the new todo."))
        }
      }

    def showAll(): F[Unit] =
      boundary.readAll.map(displayZeroOrMany)

    def searchByPartialDescription(): F[Unit] =
      descriptionPrompt
        .flatMap(boundary.readManyByPartialDescription)
        .map(displayZeroOrMany)

    def searchById(): F[Unit] =
      withIdPrompt { id =>
        boundary
          .readOneById(id)
          .map(_.to(Vector))
          .map(displayZeroOrMany)
      }

    def updateDescription(): F[Unit] =
      withIdPrompt { id =>
        withReadOne(id) { todo =>
          descriptionPrompt.map { description =>
            boundary
              .updateOne(todo.withUpdatedDescription(description))
              .as(FancyConsole[F].putSuccess("Successfully updated the description."))
          }
        }
      }

    def updateDeadline(): F[Unit] =
      withIdPrompt { id =>
        withReadOne(id) { todo =>
          withDeadlinePrompt { deadline =>
            boundary
              .updateOne(todo.withUpdatedDeadline(deadline))
              .as(FancyConsole[F].putSuccess("Successfully updated the deadline."))
          }
        }
      }

    def exit(): F[Unit] =
      FancyConsole[F].putStrLn("\nUntil next time!\n")

    def displayZeroOrMany(todos: Vector[Todo.Existing]): F[Unit] =
      if (todos.isEmpty) displayNoTodosFoundMessage()
      else {
        val uxMatters = if (todos.size == 1) "todo" else "todos"

        val renderedSize: String =
          inColor(todos.size.toString)(scala.Console.GREEN)

        FancyConsole[F]
          .putStrLn(s"\nFound $renderedSize $uxMatters:\n")
          .as {
            todos
              .sortBy(_.deadline)
              .map(renderedWithPattern)
              .foreach(println)
          }
      }

    def withDeadlinePrompt(onSuccess: LocalDateTime => Unit): F[Unit] =
      deadlinePrompt.map(toLocalDateTime).map {
        case Right(deadline) => onSuccess(deadline)
        case Left(error)     => FancyConsole[F].putError(error)
      }

    def deadlinePrompt: F[String] =
      FancyConsole[F].getStrLnTrimmedWithPrompt(
        s"Please enter a deadline in the following format $DeadlinePromptFormat:"
      )

    def toLocalDateTime(input: String): Either[String, LocalDateTime] = {
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

    def delete(): F[Unit] =
      withIdPrompt { id =>
        withReadOne(id) { todo =>
          boundary
            .deleteOne(todo)
            .as(FancyConsole[F].putSuccess("Successfully deleted the todo."))
        }
      }

    def deleteAll(): F[Unit] =
      boundary
        .deleteAll
        .as(FancyConsole[F].putSuccess("Successfully deleted all todos."))

    def withIdPrompt(onValidId: String => Unit): F[Unit] =
      idPrompt.map(toId).map {
        case Right(id)   => onValidId(id)
        case Left(error) => FancyConsole[F].putError(error)
      }

    def idPrompt: F[String] =
      FancyConsole[F].getStrLnTrimmedWithPrompt("Please enter the id:")

    def withReadOne(id: String)(onFound: Todo.Existing => Unit): F[Unit] =
      boundary
        .readOneById(id)
        .map {
          case Some(todo) => onFound(todo)
          case None       => displayNoTodosFoundMessage()
        }

    def toId(userInput: String): Either[String, String] =
      if (userInput.isEmpty || userInput.contains(" "))
        Left(
          s"\n${scala.Console.YELLOW + userInput + scala.Console.RED} is not a valid id.${scala.Console.RESET}"
        )
      else
        Right(userInput)

    def displayNoTodosFoundMessage(): F[Unit] =
      FancyConsole[F].putWarning("\nNo todos found!")

    def renderedWithPattern(todo: Todo.Existing): String = {
      def renderedId: String =
        inColor(todo.id)(scala.Console.GREEN)

      def renderedDescription: String =
        inColor(todo.description)(scala.Console.MAGENTA)

      def renderedDeadline: String =
        inColor(todo.deadline.format(pattern))(scala.Console.YELLOW)

      s"$renderedId $renderedDescription is due on $renderedDeadline."
    }

    ???
  }

  private val DeadlinePromptPattern: String =
    "yyyy-M-d H:m"

  private val DeadlinePromptFormat: String =
    inColor(DeadlinePromptPattern)(scala.Console.MAGENTA)

  private def inColor(line: String)(color: String): String =
    color + line + scala.Console.RESET
}
