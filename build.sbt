import Dependencies._

lazy val dependencies = Seq(
  scalaLogging, logback,
  slick, /*slickSLF4J,*/ slickHikaricp, mysql,
  uTest % "test", h2Database % "test"
)

lazy val root = (project in file("."))
  .settings(
    name := "speedtest-statistic",
    organization := "pw.yurchenko",
    version := "0.1.0",
    scalaVersion := "2.12.4",
    libraryDependencies ++= dependencies,
    testFrameworks += new TestFramework("utest.runner.Framework")
  )

        