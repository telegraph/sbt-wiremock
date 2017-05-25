package uk.co.telegraph.sbt.process

import java.lang.System.getProperty

import sbt.File

object ProcessId {

  val DefaultOS = "os"
  val WindowsOS = "windows"

  def extractPid(input: String, port: Int, localJar: File): Option[String] = {
    val pidPortRegex = s"\\d+ ${localJar.getAbsolutePath} --port $port".r
    pidPortRegex.findFirstIn(input).map(_.split(" ")(0))
  }

  def osName: String = Option(getProperty("os.name", DefaultOS))
    .filter   ( _.nonEmpty )
    .getOrElse(DefaultOS)

  def killPidCommand(pid: String): String = {
    osName.toLowerCase match {
      case WindowsOS =>
        s"Taskkill /PID $pid /F"
      case _ =>
        s"kill $pid"
    }
  }

}
