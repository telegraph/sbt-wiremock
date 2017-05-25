package uk.co.telegraph.sbt.resolver

trait Repository {
  val metadata:String
  val url:String
}

object Repository{
  val DefautlMavenRepoUrl = "https://repo1.maven.org/maven2"
  val MavenMetadata       = "maven-metadata.xml"

  val DefaultMavenRepository = new Repository {
    override val metadata = MavenMetadata
    override val url      = DefautlMavenRepoUrl
  }

  val DefaultRepository = DefaultMavenRepository
}
