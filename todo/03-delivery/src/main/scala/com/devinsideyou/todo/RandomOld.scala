package com.devinsideyou
package todo

trait RandomOld {
  def nextInt(n: Int): Int
}

object RandomOld {
  implicit val dsl: RandomOld = (n: Int) => scala.util.Random.nextInt(n)
}
