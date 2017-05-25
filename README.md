
#SBT Wiremock


[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Build Status](https://jenkins-prod.api-platforms.telegraph.co.uk/job/Pipeline/job/sbt-wiremock/badge/icon)](https://jenkins-prod.api-platforms.telegraph.co.uk/job/Pipeline/job/sbt-wiremock/)

Support for running [Wiremock](http://wiremock.org/) standalone instance useful for tests and integration tests. 
This plugin can be deployed locally using:
```
sbt test scripted publishLocal
```

Installation
------------

Add the following to your project/plugins.sbt file:
```
resolvers += "tmg-repo" atS3 "s3://mvn-artifacts/snapshot"
addSbtPlugin("uk.co.telegraph" % "sbt-wiremock" % "1.0.0-b+")
```

This project depends at [SBT S3 Resolver](https://github.com/frugalmechanic/fm-sbt-s3-resolver). 


Usage
------
This plugin allow us to run/configure Wiremock from SBT. The following commands are available:
 * **deploy-wiremock** - Download's the Wiremock artifact and installs it in a configured folder;
 * **start-wiremock** - Starts the Wiremock Standalone service;
 * **stop-wiremock** - Stops the Wiremock service.
 
Configuration
-------------
This plugin allow us to specify all available Wiremock [Configurations](http://wiremock.org/docs/running-standalone/):
 * **--port**Set the HTTP port number e.g. --port 9999
 * **--https-port**: If specified, enables HTTPS on the supplied port.
 * **--bind-address**: The IP address the WireMock server should serve from. Binds to all local network adapters if unspecified.
 * **--https-keystore**: Path to a keystore file containing an SSL certificate to use with HTTPS. The keystore must have a password of “password”. This option will only work if --https-port is specified. If this option isn’t used WireMock will default to its own self-signed certificate.
 * **--keystore-password**: Password to the keystore, if something other than “password”.
 * **--https-truststore**: Path to a keystore file containing client certificates. See https and proxy-client-certs for details.
 * **--truststore-password**: Optional password to the trust store. Defaults to “password” if not specified.
 * **--https-require-client-cert**: Force clients to authenticate with a client certificate. See https for details.
 * **--verbose**: Turn on verbose logging to stdout
 * **--root-dir**: Sets the root directory, under which mappings and __files reside. This defaults to the current directory.
 * **--record-mappings**: Record incoming requests as stub mappings. See record-playback.
 * **--match-headers**: When in record mode, capture request headers with the keys specified. See record-playback.
 * **--proxy-all**: Proxy all requests through to another base URL e.g. --proxy-all=\"http://api.someservice.com\" Typically used in conjunction with --record-mappings such that a session on another service can be recorded.
 * **--preserve-host-header**: When in proxy mode, it passes the Host header as it comes from the client through to the proxied service. When this option is not present, the Host header value is deducted from the proxy URL. This option is only available if the --proxy-all option is specified.
 * **--proxy-via**: When proxying requests (either by using –proxy-all or by creating stub mappings that proxy to other hosts), route via another proxy server (useful when inside a corporate network that only permits internet access via an opaque proxy). e.g. --proxy-via webproxy.mycorp.com (defaults to port 80) or --proxy-via webproxy.mycorp.com:8080
 * **--enable-browser-proxying**: Run as a browser proxy. See browser-proxying.
 * **--no-request-journal**: Disable the request journal, which records incoming requests for later verification. This allows WireMock to be run (and serve stubs) for long periods (without resetting) without exhausting the heap. The --record-mappings option isn’t available if this one is specified.
 * **--container-threads**: The number of threads created for incoming requests. Defaults to 200.
 * **--max-request-journal-entries**: Set maximum number of entries in request journal (if enabled). When this limit is reached oldest entries will be discarded.
 * **--jetty-acceptor-threads**: The number of threads Jetty uses for accepting requests.
 * **--jetty-accept-queue-size**: The Jetty queue size for accepted requests.
 * **--jetty-header-buffer-size**: The Jetty buffer size for request headers, e.g. --jetty-header-buffer-size 16384, defaults to 8192K.
 * **--print-all-network-traffic**: Print all raw incoming and outgoing network traffic to console.
 * **--global-response-templating**: Render all response definitions using Handlebars templates. --local-response-templating: Enable rendering of response definitions using Handlebars templates for specific stub mappings.
 
 Additionally:
 * **--version**: Wiremock version to download. Defaults to latest
 * **--download-dir**: The directory the Wiremock local jar will be downloaded to. Defaults to ./target.
 * **--download-ttl**: Re-download the jar if the existing one is older than this. Defaults to 2 days.
 
It is possible to start and stop Wiremock automatically before and after your tests being executed. 
To do so:
 
 ```
 startWiremock := startWiremock.dependsOn(compile in Test).value
 test in Test := (test in Test).dependsOn(startWiremock).value
 testOnly in Test := (testOnly in Test).dependsOn(startWiremock).value
 ```

Scopes
------

By default this plugin lives entirely in the `Global` scope. However, different settings for different scopes is possible. 
For instance, you can add the plugin to the `IntegrationTest` scope using

```
inConfig(IntegrationTest)(wiremockSettings)
```

You can then adjust the settings within the `IntegrationTest` scope using

```
(wiremockVerbose  in IntegrationTest) := true
(wiremockRootDir  in IntegrationTest) := baseDirectory.value / "src"/ "it" / "resources" / "wiremock"
(wiremockHttpPort in IntegrationTest) := 19999
```

and you can execute the plugin tasks within the `Test` scope using

```
sbt it:start-wiremock
```

Examples
--------
Examples of the plugin usage can be found in "src/sbt-test"

Thanks
-----

This work is based on the [SBT DynamoDB](https://github.com/localytics/sbt-dynamodb).
