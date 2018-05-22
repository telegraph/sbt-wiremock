package uk.co.telegraph.sbt.wiremock.tasks

import java.net.Socket

import sbt._
import sys.process._
import sbt.Keys.TaskStreams

import scala.util.Try

object WiremockStart {

  import uk.co.telegraph.sbt.process.ProcessId._

  private def isPortAvailable(port:Int):Boolean = {
    Try( new Socket("localhost", port).close() ).isSuccess
  }

  def apply(
    targetDir:File,
    httpPort:Int,
    httpsPort:Option[Int],
    otherArgs:Map[String, Any],
    logger:Logger
  ):String = {
    val localJar = new File(targetDir, "wiremock-standalone.jar")

    //Check ports available
    if( isPortAvailable(httpPort) ){
      sys.error(s"Port already being used [$httpPort].")
    }
    if( httpsPort.exists(isPortAvailable) ){
      sys.error(s"Port already being used [$httpPort].")
    }

    val args = Seq("java") ++
      Seq("-jar", localJar.getAbsolutePath) ++
      Seq("--port", httpPort.toString) ++
      httpsPort.map( p => Seq("--https-port", p.toString)).getOrElse(Seq.empty) ++
      otherArgs.flatMap({
        case (key, Some(value)) => Seq(key, value.toString)
        case (key, true       ) => Seq(key)
        case (key, value      ) => Seq(key, value.toString)
      })

    logger.info ("Starting Wiremock")
    logger.info(s"   Application: ${localJar.getPath}")
    logger.info(s"   Arguments  : ${args.toString}")
    Process(args).run()
    do {
      logger.info (s"Waiting for Wiremock to boot on port [$httpPort]")
      Thread.sleep( 500 )
    }while(!isPortAvailable(httpPort))

    extractPid("jps -ml".!!, httpPort, localJar).getOrElse {
      sys.error(s"Cannot find wiremock PID running on $httpPort")
    }
  }
}
