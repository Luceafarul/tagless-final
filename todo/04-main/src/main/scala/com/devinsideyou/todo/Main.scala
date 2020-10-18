package com.devinsideyou.todo

import cats.effect.IO

object Main extends App {
  Program.dsl[IO].unsafeRunSync()
}
