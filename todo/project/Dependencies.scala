import sbt._

object Dependencies {
  case object com {
    case object github {
      case object alexarchambault {
        val `scalacheck-shapeless_1.14` =
          "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.5"
      }
    }
  }

  case object dev {
    case object zio {
      val zio =
        "dev.zio" %% "zio" % "1.0.3"

      val `zio-interop-cats` =
        "dev.zio" %% "zio-interop-cats" % "2.2.0.1"
    }
  }

  case object io {
    case object monix {
      val `monix-eval` =
        "io.monix" %% "monix-eval" % "3.2.2"
    }
  }

  case object org {
    case object scalacheck {
      val scalacheck =
        "org.scalacheck" %% "scalacheck" % "1.14.3"
    }

    case object scalatest {
      val scalatest =
        "org.scalatest" %% "scalatest" % "3.1.1"
    }

    case object scalatestplus {
      val `scalatestplus-scalacheck` =
        "org.scalatestplus" %% "scalatestplus-scalacheck" % "3.1.0.0-RC2"
    }

    case object tpolecat {
      val `skunk-core` =
        "org.tpolecat" %% "skunk-core" % "0.0.20"
    }

    case object typelevel {
      val `cats-core` =
        "org.typelevel" %% "cats-core" % "2.1.1"

      val `cats-effect` =
        "org.typelevel" %% "cats-effect" % "2.2.0"

      val `kind-projector` =
        "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full
    }
  }
}
