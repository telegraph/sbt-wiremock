
import sbt._

object Dependencies {

  private lazy val json4sVersion  = "3.2.10"
  private lazy val awsSdkVersion  = "1.11.86"

  lazy val ProjectDependencies = Seq(
  )

  lazy val UnitTestDependencies = Seq(
    "junit"         %  "junit"       % "4.12"  % Test,
    "org.mockito"   % "mockito-core" % "2.7.2" % Test,
    "org.scalatest" %% "scalatest"   % "3.0.1" % Test,
    "org.scalactic" %% "scalactic"   % "3.0.1" % Test
  )
}