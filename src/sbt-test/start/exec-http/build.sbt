import java.io.ByteArrayOutputStream

lazy val checkRequest = TaskKey[Unit]("check-request", "Tests the proxy")
lazy val testDeployLatest = (project in file(".")).
  configs ( IntegrationTest         ).
  settings( Defaults.itSettings: _* ).
  settings( inConfig(IntegrationTest)(wiremockSettings): _* ).
  settings( inConfig(Test           )(wiremockSettings): _* ).
  settings(
    version         := "01",
    scalaVersion    := "2.10.6",
    (wiremockVerbose  in IntegrationTest) := true,
    (wiremockRootDir  in IntegrationTest) := baseDirectory.value / "src"/ "it" / "resources" / "wiremock",
    (wiremockHttpPort in IntegrationTest) := 19999,
    (checkRequest     in IntegrationTest) := validateRequest("it", streams.value.log, (wiremockHttpPort in IntegrationTest).value),
    (wiremockVerbose  in Test)            := true,
    (wiremockRootDir  in Test)            := baseDirectory.value / "src"/ "test" / "resources" / "wiremock",
    (wiremockHttpPort in Test)            := 29999,
    (checkRequest     in Test)            := validateRequest("test", streams.value.log, (wiremockHttpPort in Test).value)
  )


def validateRequest(env:String, log:Logger, httpPort:Int) = {
  val stream = new ByteArrayOutputStream()
  (url(s"http://127.0.0.1:$httpPort/ping") #> stream).!!

  val response = stream.toString
  if( !response.contains(s""""ping": "$env-pong"""") ){
    sys.error(s"Invalid Response: [$response]")
  }
}

