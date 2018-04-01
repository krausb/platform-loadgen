// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `streamarchitect-io-platform-loadgen` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        library.ScalaCheck,
        library.ScalaTest,
        library.TypesafeConfig,
        library.Mockito,
        library.GatlingHighcharts,
        library.GatlingTestFramework,
        library.MqttGatling,
        library.MqttPahoClient
      )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val scala          = "2.11.12"
      val scalaTest      = "3.0.1"
      val mockito        = "1.10.19"
      val scalaCheck     = "1.13.5"
      val log4j          = "2.8.1"
      val typesafeConfig = "1.3.1"

      val gatling             = "2.3.0"
      val mqttGatlingProtocol = "1.2"
      val mqttPahoClient      = "1.2.0"
    }
    val ScalaCheck     = "org.scalacheck"           %% "scalacheck"  % Version.scalaCheck % Test
    val ScalaTest      = "org.scalatest"            %% "scalatest"   % Version.scalaTest % Test
    val Log4jCore      = "org.apache.logging.log4j" % "log4j-core"   % Version.log4j
    val Log4j          = "org.apache.logging.log4j" % "log4j-api"    % Version.log4j
    val TypesafeConfig = "com.typesafe"             % "config"       % Version.typesafeConfig
    val Mockito        = "org.mockito"              % "mockito-core" % Version.mockito % Test

    val GatlingHighcharts    = "io.gatling.highcharts" % "gatling-charts-highcharts" % Version.gatling
    val GatlingTestFramework = "io.gatling"            % "gatling-test-framework"    % Version.gatling

    val MqttGatling    = "com.github.jeanadrien" %% "gatling-mqtt-protocol"         % Version.mqttGatlingProtocol
    val MqttPahoClient = "org.eclipse.paho"      % "org.eclipse.paho.client.mqttv3" % Version.mqttPahoClient
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  scalafmtSettings

lazy val commonSettings =
  Seq(
    // scalaVersion from .travis.yml via sbt-travisci
    scalaVersion := "2.11.12",
    organization := "io.streamarchitect",
    organizationName := "Bastian Kraus",
    startYear := Some(2018),
    licenses += ("GPL-3.0", url("http://www.gnu.org/licenses/gpl-3.0.en.html")),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-Ypartial-unification",
      "-Ywarn-unused-import"
    ),
    Compile / unmanagedSourceDirectories := Seq((Compile / scalaSource).value),
    Test / unmanagedSourceDirectories := Seq((Test / scalaSource).value),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    wartremoverWarnings in (Compile, compile) ++= Warts.unsafe
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true
  )
