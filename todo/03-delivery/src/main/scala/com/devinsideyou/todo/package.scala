package com.devinsideyou

package object todo {
  final implicit class PipeAndTap[A](private val a: A) extends AnyVal {
    @inline def pipe[B](ab: A => B): B = ab(a)
    @inline def tap[U](au: A => U): A = { au(a); a }
    @inline def tapAs[U](u: => U): A = { u; a }
  }
}
