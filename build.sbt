Global / useSuperShell := false
Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.4"

addCommandAlias("lint", ";scalafmtAll; scalafixAll")

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "io.innerproduct",
      scalaVersion := "2.13.4",
      semanticdbEnabled := true,
      semanticdbVersion := scalafixSemanticdb.revision
    )),
    name := "policykit",
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
    libraryDependencies ++= Seq(
      Dependencies.catsCore,
      Dependencies.catsEffect,
      Dependencies.munitTest,
      Dependencies.scalaCheck
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )
