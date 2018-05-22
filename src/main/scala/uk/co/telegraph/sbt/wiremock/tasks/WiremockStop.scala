package uk.co.telegraph.sbt.wiremock.tasks

import sbt._
import sbt.Keys.TaskStreams
import uk.co.telegraph.sbt.process.ProcessId
import sys.process._

object WiremockStop {

  def apply(targetDir: File, httpPort: Int, stream: TaskStreams): Unit = {
    val localJar = new File(targetDir, "wiremock-standalone.jar")

    ProcessId.extractPid("jps -ml".!!, httpPort, localJar) match {
      case Some(pid) =>
        stream.log.info(s"Stopping Wiremock Process [$pid]")
        ProcessId.killPidCommand(pid).!
      case None =>
        stream.log.warn("Cannot find wiremock PID")
    }
  }
}
