package handmade.cats.core

import handmade.cats.Eq

package object instances {
  object EqInstances {
    implicit val eqString: Eq[String] = (x: String, y: String) => x == y
  }
}
