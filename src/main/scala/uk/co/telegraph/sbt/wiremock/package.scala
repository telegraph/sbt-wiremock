package uk.co.telegraph.sbt

import uk.co.telegraph.sbt.resolver.Repository.DefaultRepository

import scala.language.implicitConversions

package object wiremock {

  import resolver._

  sealed trait Version{
    val label:String
  }

  private [wiremock] case class VersionImp(label:String) extends Version
  object LatestVersion  extends VersionImp(LatestVersionLabel)
  object ReleaseVersion extends VersionImp(ReleaseVersionLabel)

  implicit def toVersion(label:String):Version =
    VersionImp(label)

  implicit val repository = DefaultRepository
}
