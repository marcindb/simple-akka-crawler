organization  := "com.example"

version       := "0.1"

scalaVersion  := "2.10.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

libraryDependencies ++= Seq(
  "io.spray"            %   "spray-client" % "1.2-M8",
  "io.spray"            %   "spray-testkit" % "1.2-M8" % "test",
  "com.typesafe.akka"   %%  "akka-actor"    % "2.2.0-RC1",
  "com.typesafe.akka"   %%  "akka-testkit"  % "2.2.0-RC1" % "test",
  "org.scalatest" % "scalatest_2.10" % "2.0.M6" % "test",
  "org.jsoup"           %   "jsoup"         % "1.7.2",
  "junit"               %   "junit"         % "4.8.1" % "test",
  "com.typesafe"	    %%  "scalalogging-slf4j" % "1.0.1",
  "org.slf4j" % "slf4j-api" % "1.7.1",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.1",  // for any java classes looking for this
  "ch.qos.logback" % "logback-classic" % "1.0.3"
)

