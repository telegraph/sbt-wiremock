
import Dependencies._

lazy val buildNumber = sys.env.get("BUILD_NUMBER").map(bn => s"b$bn")
lazy val CommonSettings = Seq(
  name              := "sbt-wiremock",
  organization      := "uk.co.telegraph",
  version           := "1.1.0-" + buildNumber.getOrElse("SNAPSHOT"),
  scalaVersion      := "2.12.4",
  isSnapshot        := buildNumber.isEmpty,
  sbtPlugin         := true
)
lazy val root = (project in file(".")).
  settings(CommonSettings: _*).
  settings(
    logBuffered       in Test := false,
    parallelExecution in Test := false,
    fork              in Test := true,
    concurrentRestrictions := Seq(
      Tags.limit(Tags.Test, 1)
    )
  )


libraryDependencies ++= ProjectDependencies ++ UnitTestDependencies
publishTo := {
  if( isSnapshot.value ){
    Some("mvn-artifacts" atS3 "s3://mvn-artifacts/snapshot")
  }else {
    Some("mvn-artifacts" atS3 "s3://mvn-artifacts/release")
  }
}
