package uk.co.telegraph.sbt.resolver

import org.junit.runner.RunWith
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import sbt._

@RunWith(classOf[JUnitRunner])
class ModuleResolverTest extends FunSpec with Matchers{

  import ModuleResolverTest._

  describe("Given the ModuleResolver, "){
    describe("when no scala version is used"){
      implicit val scalaVersion = ""

      it("I should be able to deploy the latest version"){

        val resolver = ModuleResolverImp(SampleJavaModuleLatestVersion).resolveVersion()

        resolver.groupId     shouldBe SampleModuleOrganization
        resolver.artifactId  shouldBe SampleModuleArtifact
        resolver.revision    shouldBe "4.0.0-M1"
        resolver.remoteJar   shouldBe SampleJavaArtifactLatestRemoteJar
      }

      it("I should be able to deploy the release version"){

        val resolver = ModuleResolverImp(SampleJavaModuleReleasedVersion).resolveVersion()

        resolver.groupId     shouldBe SampleModuleOrganization
        resolver.artifactId  shouldBe SampleModuleArtifact
        resolver.revision    shouldBe "3.0.0"
        resolver.remoteJar   shouldBe SampleJavaArtifactReleasedRemoteJar
      }

      it("I should be able to deploy a specific version"){

        val resolver = ModuleResolverImp(SampleJavaModuleSpecificVersion).resolveVersion()

        resolver.groupId     shouldBe SampleModuleOrganization
        resolver.artifactId  shouldBe SampleModuleArtifact
        resolver.revision    shouldBe "1.0.0"
        resolver.remoteJar   shouldBe SampleJavaArtifactSpecificRemoteJar
      }

      it("I should get the latest version if the specific version does not exist"){

        val resolver = ModuleResolverImp(SampleJavaModuleInvalidVersion).resolveVersion()

        resolver.groupId     shouldBe SampleModuleOrganization
        resolver.artifactId  shouldBe SampleModuleArtifact
        resolver.revision    shouldBe "4.0.0-M1"
        resolver.remoteJar   shouldBe SampleJavaArtifactLatestRemoteJar
      }
    }

    describe("when setting scala version "){
      implicit val scalaVersion = SampleScalaVersion

      it("I should be able to deploy the latest version"){

        val resolver = ModuleResolverImp(SampleScalaModuleLatestVersion).resolveVersion()

        resolver.groupId     shouldBe SampleModuleOrganization
        resolver.artifactId  shouldBe s"${SampleModuleArtifact}_$SampleScalaVersion"
        resolver.revision    shouldBe "4.0.0-M1"
        resolver.remoteJar   shouldBe SampleScalaArtifactLatestRemoteJar
      }

      it("I should be able to deploy the release version"){

        val resolver = ModuleResolverImp(SampleScalaModuleReleasedVersion).resolveVersion()

        resolver.groupId     shouldBe SampleModuleOrganization
        resolver.artifactId  shouldBe s"${SampleModuleArtifact}_$SampleScalaVersion"
        resolver.revision    shouldBe "3.0.0"
        resolver.remoteJar   shouldBe SampleScalaArtifactReleasedRemoteJar
      }

      it("I should be able to deploy a specific version"){

        val resolver = ModuleResolverImp(SampleScalaModuleSpecificVersion).resolveVersion()

        resolver.groupId     shouldBe SampleModuleOrganization
        resolver.artifactId  shouldBe s"${SampleModuleArtifact}_$SampleScalaVersion"
        resolver.revision    shouldBe "1.0.0"
        resolver.remoteJar   shouldBe SampleScalaArtifactSpecificRemoteJar
      }

      it("I should get the latest version if the specific version does not exist"){

        val resolver = ModuleResolverImp(SampleScalaModuleInvalidVersion).resolveVersion()

        resolver.groupId     shouldBe SampleModuleOrganization
        resolver.artifactId  shouldBe s"${SampleModuleArtifact}_$SampleScalaVersion"
        resolver.revision    shouldBe "4.0.0-M1"
        resolver.remoteJar   shouldBe SampleScalaArtifactLatestRemoteJar
      }
    }
  }
}

object ModuleResolverTest{

  val SampleScalaVersion       = "2.11"
  val SampleModuleOrganization = "uk.co.telegraph"
  val SampleModuleArtifact     = "sample-project"
  val SampleRepoPath           = getClass.getResource("/repo").toURI.toString
  val SampleGroupIdPath        = SampleModuleOrganization.replaceAll("\\.", "/")

  val SampleJavaModuleLatestVersion       = SampleModuleOrganization %  SampleModuleArtifact % "latest"
  val SampleJavaModuleReleasedVersion     = SampleModuleOrganization %  SampleModuleArtifact % "release"
  val SampleJavaModuleSpecificVersion     = SampleModuleOrganization %  SampleModuleArtifact % "1.0.0"
  val SampleJavaModuleInvalidVersion      = SampleModuleOrganization %  SampleModuleArtifact % "5.0.0"

  val SampleJavaArtifactLatestRemoteJar   = url(s"$SampleRepoPath/$SampleGroupIdPath/$SampleModuleArtifact/4.0.0-M1/$SampleModuleArtifact-4.0.0-M1.jar")
  val SampleJavaArtifactReleasedRemoteJar = url(s"$SampleRepoPath/$SampleGroupIdPath/$SampleModuleArtifact/3.0.0/$SampleModuleArtifact-3.0.0.jar")
  val SampleJavaArtifactSpecificRemoteJar = url(s"$SampleRepoPath/$SampleGroupIdPath/$SampleModuleArtifact/1.0.0/$SampleModuleArtifact-1.0.0.jar")

  val SampleScalaModuleLatestVersion       = SampleModuleOrganization %% SampleModuleArtifact % "latest"
  val SampleScalaModuleReleasedVersion     = SampleModuleOrganization %% SampleModuleArtifact % "release"
  val SampleScalaModuleSpecificVersion     = SampleModuleOrganization %% SampleModuleArtifact % "1.0.0"
  val SampleScalaModuleInvalidVersion      = SampleModuleOrganization %% SampleModuleArtifact % "5.0.0"

  val SampleScalaArtifactLatestRemoteJar   = url(s"$SampleRepoPath/$SampleGroupIdPath/${SampleModuleArtifact}_$SampleScalaVersion/4.0.0-M1/${SampleModuleArtifact}_$SampleScalaVersion-4.0.0-M1.jar")
  val SampleScalaArtifactReleasedRemoteJar = url(s"$SampleRepoPath/$SampleGroupIdPath/${SampleModuleArtifact}_$SampleScalaVersion/3.0.0/${SampleModuleArtifact}_$SampleScalaVersion-3.0.0.jar")
  val SampleScalaArtifactSpecificRemoteJar = url(s"$SampleRepoPath/$SampleGroupIdPath/${SampleModuleArtifact}_$SampleScalaVersion/1.0.0/${SampleModuleArtifact}_$SampleScalaVersion-1.0.0.jar")

  val LocalRepository = new Repository {
    override val metadata = "maven-metadata.xml"
    override val url = SampleRepoPath
  }
  case class ModuleResolverImp(module:ModuleID)(implicit val scalaVersion:String) extends ModuleResolver{
    implicit val repository:Repository = LocalRepository
  }
}
