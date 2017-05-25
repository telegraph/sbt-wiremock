
lazy val testDeployLatest = (project in file(".")).
  settings(
    version      := "01",
    scalaVersion := "2.10.6"
  )

