import java.net.Socket

import scala.util.Try

lazy val checkPort = TaskKey[Unit]("check-port", "This task validates that the ports are being used")

lazy val testDeployLatest = (project in file(".")).
  settings(
    version      := "01",
    scalaVersion := "2.10.6",
    checkPort := {
      val logger                = streams.value.log
      val httpPort:Int          = wiremockHttpPort.value
      val httpsPort:Option[Int] = wiremockHttpsPort.value
      (Seq(httpPort) ++ httpsPort.map(Seq(_)).getOrElse(Nil))
        .foreach( port => {
          logger.info(s"Testing Port: [$httpPort]")
          if( !isPortAvailable(port) ){
            sys.error(s"Port not being used: [$httpPort]")
          }
        })
    }
  )


def isPortAvailable(port:Int):Boolean = {
  Try{ new Socket("127.0.0.1", port).close() }.isSuccess
}

