import Dependencies._

enablePlugins(JavaAppPackaging)

lazy val dependencies = Seq(
  scalaLogging, logback,
  slick, slickHikaricp, mysql,
  uTest % "test", h2Database % "test"
)

lazy val buildConfig = Seq(
  name in Universal := name.value,
  packageName in Universal := packageName.value,
  mappings in Universal in packageBin += file("src/main/resources/application.conf") -> "conf/application.conf",
  mappings in Universal in packageBin += file("src/main/resources/logback.xml") -> "conf/logback.xml",
  mappings in Universal in packageBin += file("src/main/service/pinger.service") -> "service/pinger.service",
  bashScriptExtraDefines ++= Seq(
    """addJava "-Dconfig.file=${app_home}/../conf/application.conf" """,
    """addJava "-Dlogback.configurationFile=${app_home}/../conf/logback.xml" """
  )
)

lazy val root = (project in file("."))
  .settings(
    name := "speedtest-statistic",
    organization := "pw.yurchenko",
    version := "0.1.0",
    scalaVersion := "2.12.4",
    libraryDependencies ++= dependencies,
    dependencyOverrides ++= overrides,
    testFrameworks += new TestFramework("utest.runner.Framework"),
    buildConfig
  )

        