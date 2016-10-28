import com.timgroup.sbtjavaversion.SbtJavaVersionKeys._
import com.typesafe.sbteclipse.core.EclipsePlugin.{EclipseKeys, EclipseProjectFlavor}

name := "blank-java-worker-app"

organization := "com.timgroup"

version := "0.0." + sys.env.getOrElse("BUILD_NUMBER", "0-SNAPSHOT")

javaVersion in ThisBuild := "1.8"

javacOptions += "-g"
javacOptions += "-parameters"

publishTo in ThisBuild := Some("publish-repo" at "http://repo.youdevise.com:8081/nexus/content/repositories/yd-release-candidates")

credentials in ThisBuild += Credentials(new File("/etc/sbt/credentials"))

crossPaths := false

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

EclipseKeys.withSource := true

resolvers += "TIM Group Repo" at "http://repo/nexus/content/groups/public"

UploadToProductstore.uploadToProductstoreSettings

libraryDependencies := Seq(
  "com.timgroup" % "Tucker" % autobump,
  "com.timgroup" % "tim-logger" % autobump,
  "com.timgroup" % "tim-structured-events" % autobump,
  "com.timgroup" % "tim-structured-events-testing" % autobump % "test",
  "com.typesafe" % "config" % "1.2.1",
  "io.dropwizard.metrics" % "metrics-core" % "3.1.2",
  "io.dropwizard.metrics" % "metrics-jvm" % "3.1.2",
  "io.dropwizard.metrics" % "metrics-graphite" % "3.1.2",

  "junit" % "junit" % "4.12" % "test",
  "org.hamcrest" % "hamcrest-core" % "1.3" % "test",
  "org.hamcrest" % "hamcrest-library" % "1.3" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test"
  )

publishArtifact in (Compile, packageDoc) := false

overridePublishSettings