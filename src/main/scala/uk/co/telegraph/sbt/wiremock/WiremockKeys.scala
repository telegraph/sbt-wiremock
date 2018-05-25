package uk.co.telegraph.sbt.wiremock

import sbt._
import uk.co.telegraph.sbt.resolver._

import scala.concurrent.duration.Duration

trait WiremockKeys {

  lazy val wiremockVersion                  = SettingKey[Version ]("version-wiremock", "Wiremock version to download. Defaults to latest")
  lazy val wiremockDownloadDir              = SettingKey[File    ]("download-dir", "The directory the Wiremock local jar will be downloaded to. Defaults to ./target.")
  lazy val wiremockDownloadIfOlderThan      = SettingKey[Duration]("download-ttl", "Re-download the jar if the existing one is older than this. Defaults to 2 days.")
  lazy val wiremockHttpPort                 = SettingKey[Int           ]("port", "Set the HTTP port number e.g. --port 9999")
  lazy val wiremockHttpsPort                = SettingKey[Option[Int]   ]("https-port", "If specified, enables HTTPS on the supplied port.")
  lazy val wiremockBindAddress              = SettingKey[String        ]("bind-address", "The IP address the WireMock server should serve from. Binds to all local network adapters if unspecified.")
  lazy val wiremockHttpsKeystore            = SettingKey[Option[File]  ]("https-keystore", "Path to a keystore file containing an SSL certificate to use with HTTPS. The keystore must have a password of “password”. This option will only work if --https-port is specified. If this option isn’t used WireMock will default to its own self-signed certificate.")
  lazy val wiremockKeystorePassword         = SettingKey[Option[String]]("keystore-password", "Password to the keystore, if something other than “password”.")
  lazy val wiremockHttpsTruststore          = SettingKey[Option[File]  ]("https-truststore", "Path to a keystore file containing client certificates. See https and proxy-client-certs for details.")
  lazy val wiremockTruststorePassword       = SettingKey[Option[String]]("truststore-password", "Optional password to the trust store. Defaults to “password” if not specified.")
  lazy val wiremockHttpsRequiredClientCert  = SettingKey[Boolean       ]("https-require-client-cert", "Force clients to authenticate with a client certificate. See https for details.")
  lazy val wiremockVerbose                  = SettingKey[Boolean       ]("verbose", "Turn on verbose logging to stdout")
  lazy val wiremockRootDir                  = SettingKey[File          ]("root-dir", "Sets the root directory, under which mappings and __files reside. This defaults to the current directory.")
  lazy val wiremockRecordMapping            = SettingKey[Boolean       ]("record-mappings", "Record incoming requests as stub mappings. See record-playback.")
  lazy val wiremockMatchHeaders             = SettingKey[Option[String]]("match-headers", "When in record mode, capture request headers with the keys specified. See record-playback.")
  lazy val wiremockProxyAll                 = SettingKey[Option[URL]   ]("proxy-all", "Proxy all requests through to another base URL e.g. --proxy-all=\"http://api.someservice.com\" Typically used in conjunction with --record-mappings such that a session on another service can be recorded.")
  lazy val wiremockPreserveHostHeader       = SettingKey[Boolean       ]("preserve-host-header", "When in proxy mode, it passes the Host header as it comes from the client through to the proxied service. When this option is not present, the Host header value is deducted from the proxy URL. This option is only available if the --proxy-all option is specified.")
  lazy val wiremockProxyVia                 = SettingKey[Option[URL]   ]("proxy-via", "When proxying requests (either by using –proxy-all or by creating stub mappings that proxy to other hosts), route via another proxy server (useful when inside a corporate network that only permits internet access via an opaque proxy). e.g. --proxy-via webproxy.mycorp.com (defaults to port 80) or --proxy-via webproxy.mycorp.com:8080")
  lazy val wiremockEnableBrowserProxying    = SettingKey[Boolean       ]("enable-browser-proxying", "Run as a browser proxy. See browser-proxying.")
  lazy val wiremockNoRequestJournal         = SettingKey[Boolean       ]("no-request-journal", "Disable the request journal, which records incoming requests for later verification. This allows WireMock to be run (and serve stubs) for long periods (without resetting) without exhausting the heap. The --record-mappings option isn’t available if this one is specified.")
  lazy val wiremockContainerThreads         = SettingKey[Option[Int]   ]("container-threads", "The number of threads created for incoming requests. Defaults to 200.")
  lazy val wiremockMaxRequestJournalEntries = SettingKey[Option[Int]   ]("max-request-journal-entries", "Set maximum number of entries in request journal (if enabled). When this limit is reached oldest entries will be discarded.")
  lazy val wiremockJettyAcceptorThreads     = SettingKey[Option[Int]   ]("jetty-acceptor-threads", "The number of threads Jetty uses for accepting requests.")
  lazy val wiremockJettyAcceptQueueSize     = SettingKey[Option[Int]   ]("jetty-accept-queue-size", "The Jetty queue size for accepted requests.")
  lazy val wiremockJettyHeaderBufferSize    = SettingKey[Option[Int]   ]("jetty-header-buffer-size", "The Jetty buffer size for request headers, e.g. --jetty-header-buffer-size 16384, defaults to 8192K.")
//  lazy val wiremockExtensions               = SettingKey[String        ]("extensions", "Extension class names e.g. com.mycorp.HeaderTransformer,com.mycorp.BodyTransformer. See extending-wiremock.")
  lazy val wiremockPrintAllNetworkTraffic   = SettingKey[Boolean       ]("print-all-network-traffic", "Print all raw incoming and outgoing network traffic to console.")
  lazy val wiremockGlobalResponseTemplating = SettingKey[Boolean       ]("global-response-templating", "Render all response definitions using Handlebars templates. --local-response-templating: Enable rendering of response definitions using Handlebars templates for specific stub mappings.")

  lazy val deployWiremock = TaskKey[File]("deploy-wiremock")
  lazy val startWiremock  = TaskKey[String]("start-wiremock")
  lazy val stopWiremock   = TaskKey[Unit]("stop-wiremock")
}
