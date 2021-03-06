package uk.co.telegraph.sbt.resolver

import java.net.URL

import sbt.CrossVersion._
import sbt._

import scala.xml.XML

trait ModuleResolver {

  implicit val repository  :Repository
  implicit val scalaVersion:String
  val module:ModuleID

  lazy val groupId      = module.organization
  lazy val artifactId   = module.crossVersion match {
    case Disabled => module.name
    case _        => s"${module.name}_$scalaVersion"
  }
  lazy val revision   = module.revision

  private [resolver] lazy val metadata = loadMetadata


  //Check if version exists
  def checkVersion(version:String) =
    metadata.versions.contains(version)

  def resolveVersion(versionOpt:Option[String] = None):ModuleResolver = {
    val resolvedVersion:Option[String] = versionOpt.orElse( Some(revision) )
    val versionLabel = resolvedVersion
      .map({
        case LatestVersionLabel => metadata.latest
        case ReleaseVersionLabel => metadata.release
        case version   => version
      })
      .filter(checkVersion)
      .getOrElse(metadata.latest)

    fromModuleIdAndMetadata(module.copy(revision = versionLabel), metadata)
  }

  //Get remove Jar
  def remoteJar:URL = artifactUrl

  private [resolver] def metadataUrl:URL =
    url(s"${repository.url}/${groupId.replaceAll("\\.", "/")}/$artifactId/${repository.metadata}")

  private [resolver] def artifactUrl:URL =
    url(s"${repository.url}/${groupId.replaceAll("\\.", "/")}/$artifactId/$revision/$artifactId-$revision.jar")

  private [resolver] def loadMetadata:ModuleMetadata = {
    val versioning = XML.load( metadataUrl ) \\ "versioning"
    val versions   = (versioning \ "versions" \ "version").map(_.text)
    val release    = (versioning \ "release").text
    val latest     = (versioning \ "latest").text

    ModuleMetadata(versions, release, latest)
  }
}