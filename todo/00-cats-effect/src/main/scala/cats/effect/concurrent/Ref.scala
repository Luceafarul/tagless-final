package cats.effect.concurrent

import java.util.concurrent.atomic.AtomicReference

import cats.effect.Sync
import cats.implicits._

import scala.annotation.tailrec

trait Ref[F[_], A] {
  def get: F[A]
  def set(a: A): F[Unit]

  def update(aa: A => A): F[Unit]
  def updateAndGet(aa: A => A): F[A]
  def modify[B](aab: A => (A, B)): F[B]
}

object Ref {
  def of[F[_] : Sync, A](a: A): F[Ref[F, A]] = Sync[F].delay {
    new Ref[F, A] {
      private[this] val state: AtomicReference[A] = new AtomicReference(a)

      override def get: F[A] = Sync[F].delay(state.get)

      override def set(a: A): F[Unit] = Sync[F].delay(state.set(a))

      override def update(aa: A => A): F[Unit] = updateAndGet(aa).void

      override def updateAndGet(aa: A => A): F[A] = modify { a =>
        val desiredState = aa(a)
        val result = desiredState

        desiredState -> result
      }

      override def modify[B](aab: A => (A, B)): F[B] = {
        @tailrec
        def setOrDieTrying: B = {
          val currentState = state.get
          val (desiredState, result) = aab(currentState)

          if (state.compareAndSet(currentState, desiredState)) result
          else setOrDieTrying
        }

        Sync[F].delay(setOrDieTrying)
      }
    }
  }
}
