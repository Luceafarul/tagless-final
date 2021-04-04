package com.devinsideyou.todo

import handmade.cats.effect.IO

object Main extends App {
  Program.dsl[IO].unsafeRunSync()
}
