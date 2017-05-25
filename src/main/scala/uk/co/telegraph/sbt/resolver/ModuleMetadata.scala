package uk.co.telegraph.sbt.resolver

private [resolver] case class ModuleMetadata(
  versions:Seq[String],
  release:String,
  latest:String
)
