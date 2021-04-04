package com.devinsideyou.todo.crud

import com.devinsideyou.Todo
import com.devinsideyou.todo.{Console, FancyConsole, Random}
import handmade.cats._
import handmade.cats.core.Functor
import handmade.cats.core.implicits._
import handmade.cats.core.instances.TraverseInstances._

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

trait Controller[F[_]] {
  def program: F[Unit]
}

object Controller {
  def dsl[F[_]: Functor : Monad](
    boundary: Boundary[F],
    pattern: DateTimeFormatter,
    fancyConsole: FancyConsole[F],
    random: Random[F]
  ): Controller[F] = new Controller[F] {
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
        menu.flatMap { m => fancyConsole.getStrLnTrimmedWithPrompt(m) }

      prompt.flatMap {
        case "c" => create().as(true)
        case "d" => delete().as(true)
        case "da" => deleteAll().as(true)
        case "sa" => showAll().as(true)
        case "sd" => searchByPartialDescription().as(true)
        case "sid" => searchById().as(true)
        case "ud" => updateDescription().as(true)
        case "udl" => updateDeadline().as(true)
        case "e" | "q" | "exit" | "quit" => exit().as(false)
        case _ => true.pure[F]
      }.iterateWhile(identity).void
    }

    def descriptionPrompt: F[String] =
      fancyConsole.getStrLnTrimmedWithPrompt("Please enter a description:")

    def create(): F[Unit] =
      descriptionPrompt.flatMap { description =>
        withDeadlinePrompt { deadline =>
          boundary.createOne(Todo.Data(description, deadline)) >>
            fancyConsole.putSuccess("Successfully created the new todo.")
        }
      }

    def delete(): F[Unit] =
      withIdPrompt { id =>
        withReadOne(id) { todo =>
          boundary.deleteOne(todo) >>
            fancyConsole.putSuccess("Successfully deleted the todo.")
        }
      }

    def deleteAll(): F[Unit] =
      boundary.deleteAll >> fancyConsole.putSuccess("Successfully deleted all todos.")

    def showAll(): F[Unit] =
      boundary.readAll.flatMap(displayZeroOrMany)

    def searchByPartialDescription(): F[Unit] =
      descriptionPrompt
        .flatMap(boundary.readManyByPartialDescription)
        .flatMap(displayZeroOrMany)

    def searchById(): F[Unit] =
      withIdPrompt { id =>
        boundary
          .readOneById(id)
          .map(_.to(Vector))
          .flatMap(displayZeroOrMany)
      }

    def updateDescription(): F[Unit] =
      withIdPrompt { id =>
        withReadOne(id) { todo =>
          descriptionPrompt.flatMap { description =>
            boundary.updateOne(todo.withUpdatedDescription(description)) >>
              fancyConsole.putSuccess("Successfully updated the description.")
          }
        }
      }

    def updateDeadline(): F[Unit] =
      withIdPrompt { id =>
        withReadOne(id) { todo =>
          withDeadlinePrompt { deadline =>
            boundary.updateOne(todo.withUpdatedDeadline(deadline)) >>
              fancyConsole.putSuccess("Successfully updated the deadline.")
          }
        }
      }

    def exit(): F[Unit] =
      fancyConsole.putStrLn("\nUntil next time!\n")

    def displayZeroOrMany(todos: Vector[Todo.Existing]): F[Unit] =
      if (todos.isEmpty) displayNoTodosFoundMessage()
      else {
        val uxMatters = if (todos.size == 1) "todo" else "todos"

        val renderedSize: String =
          inColor(todos.size.toString)(scala.Console.GREEN)

        fancyConsole.putStrLn(s"\nFound $renderedSize $uxMatters:\n") >>
          todos
            .sortBy(_.deadline)
            .map(renderedWithPattern)
            .traverse(fancyConsole.putStrLn)
            .void
      }

    def withDeadlinePrompt(onSuccess: LocalDateTime => F[Unit]): F[Unit] =
      deadlinePrompt.map(toLocalDateTime).flatMap {
        case Right(deadline) => onSuccess(deadline)
        case Left(error) => fancyConsole.putError(error)
      }

    def deadlinePrompt: F[String] =
      fancyConsole.getStrLnTrimmedWithPrompt(
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
          s"\n${inColor(input)(scala.Console.YELLOW)} does not match the required format " +
            s"$DeadlinePromptFormat.${scala.Console.RESET}"
        }
    }

    def withIdPrompt(onValidId: String => F[Unit]): F[Unit] =
      idPrompt.map(toId).flatMap {
        case Right(id) => onValidId(id)
        case Left(error) => fancyConsole.putError(error)
      }

    def toId(userInput: String): Either[String, String] =
      if (userInput.isEmpty || userInput.contains(" "))
        Left(s"\n${scala.Console.YELLOW + userInput + scala.Console.RED} " +
          s"is not a valid id.${scala.Console.RESET}")
      else Right(userInput)

    def idPrompt: F[String] =
      fancyConsole.getStrLnTrimmedWithPrompt("Please enter the id:")

    def withReadOne(id: String)(onFound: Todo.Existing => F[Unit]): F[Unit] =
      boundary
        .readOneById(id)
        .flatMap {
          case Some(todo) => onFound(todo)
          case None => displayNoTodosFoundMessage()
        }

    def displayNoTodosFoundMessage(): F[Unit] =
      fancyConsole.putWarning("\nNo todos found!")

    def renderedWithPattern(todo: Todo.Existing): String = {
      def renderedId: String =
        inColor(todo.id)(scala.Console.GREEN)

      def renderedDescription: String =
        inColor(todo.description)(scala.Console.MAGENTA)

      def renderedDeadline: String =
        inColor(todo.deadline.format(pattern))(scala.Console.YELLOW)

      s"$renderedId $renderedDescription is due on $renderedDeadline."
    }
  }

  private val DeadlinePromptPattern: String =
    "yyyy-M-d H:m"

  private val DeadlinePromptFormat: String =
    inColor(DeadlinePromptPattern)(scala.Console.MAGENTA)

  private def inColor(line: String)(color: String): String =
    color + line + scala.Console.RESET
}
