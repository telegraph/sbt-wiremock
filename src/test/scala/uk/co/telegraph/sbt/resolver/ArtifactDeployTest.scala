package uk.co.telegraph.sbt.resolver

import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FunSpec, Matchers}
import sbt._
import uk.co.telegraph.sbt.wiremock.LatestVersion

import scala.concurrent.duration._
import scala.language.postfixOps

@RunWith(classOf[JUnitRunner])
class ArtifactDeployTest extends FunSpec with Matchers with BeforeAndAfter{

  import ArtifactDeployTest._

  implicit val scalaVersion = SampleScalaVersion
  implicit val repository   = SampleRepository

  before{
    println("Before: Clean artifacts")
    IO.delete(SampleTargetPath)
  }

  after{
    println("After: Clean artifacts")
    IO.delete(SampleTargetPath)
  }

  describe("Given the WiremockDeployTask, If the project does not exist"){
    it("I should get an error if the file is not a valid Jar"){
      val error = intercept[RuntimeException](
        ArtifactDeploy(SampleInvalidModule, SampleTargetPath, 1.millisecond, SampleMockLogger)
      )
      error.getMessage should include ("Invalid jar file at")
    }

    it("I should be able to deploy the latest version"){
      val result = ArtifactDeploy(SampleModule, SampleTargetPath, 1.millisecond, SampleMockLogger)
      result shouldBe SampleModuleFile
      result.exists() shouldBe true
    }

    it("I should be able to deploy the latest version even if the folder is already there"){
      IO.createDirectory(SampleTargetPath)

      val result = ArtifactDeploy(SampleModule, SampleTargetPath, 1.millisecond, SampleMockLogger)
      result shouldBe SampleModuleFile
      result.exists() shouldBe true
    }

    it("I should get an error if no remote Jar file is available"){
      val error = intercept[RuntimeException](
        ArtifactDeploy(SampleNonExistingModule, SampleTargetPath, 1.millisecond, SampleMockLogger)
      )
      error.getMessage should include ("Fail to download artifact")
    }
  }

  describe("Given the WiremockDeployTask, If the project already exists"){
    it("it should stale the latest version if it is a old download"){
      val artifact1 = ArtifactDeploy(SampleModule, SampleTargetPath, 1.millisecond, SampleMockLogger)
      val created = artifact1.lastModified()

      artifact1.exists() shouldBe true
      Thread.sleep(1000)

      val artifact2 = ArtifactDeploy(SampleModule, SampleTargetPath, 1.millisecond, SampleMockLogger)
      val updated = artifact2.lastModified()

      artifact2.exists() shouldBe true
      updated should be > created
    }

//    it("it should not stale the latest version if it is not an old download"){
//      val artifact1 = ArtifactDeploy(SampleModule, SampleTargetPath, 1.millisecond, SampleMockLogger)
//      val created = artifact1.lastModified()
//
//      artifact1.exists() shouldBe true
//
//      val artifact2 = ArtifactDeploy(SampleModule, SampleTargetPath, 1.days, SampleMockLogger)
//      val updated = artifact2.lastModified()
//
//      artifact2.exists() shouldBe true
//      updated shouldBe created
//    }
  }
}

object ArtifactDeployTest{

  val SampleTargetPath        = new File("target/sample")
  val SampleMockLogger        = Mockito.mock(classOf[Logger])
  val SampleModuleGroupId     = "uk.co.telegraph"
  val SampleModuleArtifactId  = "sample-project"
  val SampleModuleVersion     = LatestVersion.label
  val SampleModule            = SampleModuleGroupId % SampleModuleArtifactId % SampleModuleVersion
  val SampleInvalidModule     = SampleModuleGroupId % SampleModuleArtifactId % "3.0.0"
  val SampleNonExistingModule = SampleModuleGroupId % SampleModuleArtifactId % "2.0.0"

  val SampleRepoPath      = getClass.getResource("/repo").toURI.toString
  val SampleModuleFile    = new File(s"target/sample/$SampleModuleArtifactId.jar")
  val SampleRepository    = new Repository {
    override val metadata = "maven-metadata.xml"
    override val url = SampleRepoPath
  }
  val SampleScalaVersion = "2.11.6"
}
