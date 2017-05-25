package uk.co.telegraph.sbt

import sbt.ModuleID

import scala.language.implicitConversions

package object resolver {

  import Repository._

  implicit def fromModuleIdAndMetadata(moduleId: ModuleID, metadataArg:ModuleMetadata)(implicit scalaVrs:String, mavenRepo:Repository = DefaultRepository): ModuleResolver = {
    new ModuleResolver{
      override implicit val repository   = mavenRepo
      override implicit val scalaVersion = scalaVrs
      override val module                = moduleId
      override lazy val metadata         = metadataArg
    }
  }

  implicit def fromModuleId(moduleId: ModuleID)(implicit scalaVrs:String, mavenRepo:Repository = DefaultRepository):ModuleResolver = {
    new ModuleResolver{
      override implicit val repository   = mavenRepo
      override implicit val scalaVersion = scalaVrs
      override val module = moduleId
    }
  }

  val LatestVersionLabel  = "latest"
  val ReleaseVersionLabel = "release"

}
