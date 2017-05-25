package uk.co.telegraph.sbt.wiremock

import sbt.Keys._
import sbt._
import uk.co.telegraph.sbt.wiremock.tasks.{WiremockStart, WiremockStop}

import scala.concurrent.duration._

object WiremockPlugin extends AutoPlugin{


  val DefaultGroupId = "com.github.tomakehurst"
  val DefaultArtifactId = "wiremock-standalone"

  object autoImport extends WiremockKeys {
    lazy val wiremockSettings = wiremockDefaultSettings ++ wiremockBaseTasks
  }

  override val trigger = allRequirements

  import autoImport._
  import uk.co.telegraph.sbt.resolver._

  private val wiremockDefaultSettings: Seq[Setting[_]] = Seq(
    // Custom properties
    wiremockVersion                  := LatestVersion,
    wiremockDownloadDir              := target.value,
    wiremockDownloadIfOlderThan      := 2.days,
    //Wiremock Arguments
    wiremockHttpPort                 := 9999,
    wiremockHttpsPort                := None,
    wiremockBindAddress              := "0.0.0.0",
    wiremockHttpsKeystore            := None,
    wiremockKeystorePassword         := None,
    wiremockHttpsTruststore          := None,
    wiremockTruststorePassword       := None,
    wiremockHttpsRequiredClientCert  := false,
    wiremockVerbose                  := false,
    wiremockRootDir                  := baseDirectory.value / "src"/ "it" / "resources" / "wiremock",
    wiremockRecordMapping            := false,
    wiremockMatchHeaders             := None,
    wiremockProxyAll                 := None,
    wiremockPreserveHostHeader       := false,
    wiremockProxyVia                 := None,
    wiremockEnableBrowserProxying    := false,
    wiremockNoRequestJournal         := false,
    wiremockContainerThreads         := None,
    wiremockMaxRequestJournalEntries := None,
    wiremockJettyAcceptorThreads     := None,
    wiremockJettyAcceptQueueSize     := None,
    wiremockJettyHeaderBufferSize    := None,
//    wiremockExtensions               := "",
    wiremockPrintAllNetworkTraffic   := false,
    wiremockGlobalResponseTemplating := false
  )

  val wiremockBaseTasks: Seq[Setting[_]] = Seq(
    deployWiremock := {
      implicit val scalaVersion = Keys.scalaVersion.value
      ArtifactDeploy(
        moduleId            = DefaultGroupId % DefaultArtifactId % wiremockVersion.value.label,
        targetDir           = wiremockDownloadDir.value,
        downloadIfOrderThan = wiremockDownloadIfOlderThan.value,
        logger              = streams.value.log
      )
    },
    startWiremock := {
      def excludeNonSetArgs:PartialFunction[(String, Any), Boolean] = {
        case (_, false) => false
        case (_, None)  => false
        case _          => true
      }
      val logger = streams.value.log
      WiremockStart(
        targetDir = wiremockDownloadDir.value,
        httpPort = wiremockHttpPort.value,
        httpsPort = wiremockHttpsPort.value,
        otherArgs = Map(
            "--bind-address" -> wiremockBindAddress.value,
            "--https-keystore" -> wiremockHttpsKeystore.value,
            "--keystore-password" -> wiremockKeystorePassword.value,
            "--https-truststore" -> wiremockHttpsTruststore.value,
            "--truststore-password" -> wiremockTruststorePassword.value,
            "--https-require-client-cert" -> wiremockHttpsRequiredClientCert.value,
            "--verbose" -> wiremockVerbose.value,
            "--root-dir" -> wiremockRootDir.value,
            "--record-mappings" -> wiremockRecordMapping.value,
            "--match-headers" -> wiremockMatchHeaders.value,
            "--proxy-all" -> wiremockProxyAll.value,
            "--preserve-host-header" -> wiremockPreserveHostHeader.value,
            "--proxy-via" -> wiremockProxyVia.value,
            "--enable-browser-proxying" -> wiremockEnableBrowserProxying.value,
            "--no-request-journal" -> wiremockNoRequestJournal.value,
            "--container-threads" -> wiremockContainerThreads.value,
            "--max-request-journal-entries" -> wiremockMaxRequestJournalEntries.value,
            "--jetty-acceptor-threads" -> wiremockJettyAcceptorThreads.value,
            "--jetty-accept-queue-size" -> wiremockJettyAcceptQueueSize.value,
            "--jetty-header-buffer-size" -> wiremockJettyHeaderBufferSize.value,
            "--print-all-network-traffic" -> wiremockPrintAllNetworkTraffic.value,
            "--global-response-templating" -> wiremockGlobalResponseTemplating.value
          ).filter( excludeNonSetArgs ),
        logger = logger
      )
    },
    stopWiremock := WiremockStop(
      targetDir = wiremockDownloadDir.value,
      httpPort  = wiremockHttpPort.value,
      stream    = streams.value
    )
  )

  override lazy val projectSettings: Seq[Setting[_]] = wiremockSettings
}
