import sbt._

/**
  * The object contains references on all dependencies of the project
  */
object Dependencies {

  //logging
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"

  // slick framework for working to database
  lazy val slick = "com.typesafe.slick" %% "slick" % "3.2.1"
  lazy val slickSLF4J = "org.slf4j" % "slf4j-nop" % "1.6.4"
  lazy val slickHikaricp =  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"

  // database drivers
  lazy val mysql = "mysql" % "mysql-connector-java" % "5.1.45"

  // testing
  lazy val uTest = "com.lihaoyi" %% "utest" % "0.6.0"
  lazy val h2Database = "com.h2database" % "h2" % "1.4.196"

}
