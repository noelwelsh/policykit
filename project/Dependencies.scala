import sbt._

object Dependencies {
  // Library Versions
  val catsVersion       = "2.2.0"
  val catsEffectVersion = "2.2.0"
  val munitTestVersion  = "0.7.19"
  val scalaCheckVersion = "1.14.0"

  // Libraries
  val catsCore     = "org.typelevel"  %% "cats-core"     % catsVersion
  val catsEffect   = "org.typelevel"  %% "cats-effect"   % catsEffectVersion
  val munitTest    = "org.scalameta"  %% "munit"         % munitTestVersion  % "test"
  val scalaCheck   = "org.scalacheck" %% "scalacheck"    % scalaCheckVersion % "test"
}
