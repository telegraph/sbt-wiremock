
lazy val testDeploySpecific = (project in file(".")).
  settings(
    version         := "01",
    scalaVersion    := "2.10.6",
    wiremockVersion := "2.4.1"
  )

