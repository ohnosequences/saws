
import sbtrelease._
import ReleaseStateTransformations._
import ReleasePlugin._
import ReleaseKeys._


name := "saws"

organization := "ohnosequences"

scalaVersion := "2.10.1"


publishMavenStyle := false

publishTo <<= (isSnapshot, s3resolver) { 
                (snapshot,   resolver) => 
  val prefix = if (snapshot) "snapshots" else "releases"
  resolver("Era7 "+prefix+" S3 bucket", "s3://"+prefix+".era7.com")
}

resolvers ++= Seq ( 
    Resolver.typesafeRepo("releases")
  , Resolver.sonatypeRepo("releases")
  , Resolver.sonatypeRepo("snapshots")
  , "Era7 Releases"  at "http://releases.era7.com.s3.amazonaws.com"
  , "Era7 Snapshots" at "http://snapshots.era7.com.s3.amazonaws.com"
  )

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "1.2.4",
  "com.amazonaws" % "aws-java-sdk" % "1.3.26",
  "com.novocode" % "junit-interface" % "0.10-M1" % "test",
  "net.databinder.dispatch" %% "dispatch-core" % "0.10.0"
)

scalacOptions ++= Seq(
                      "-feature",
                      "-language:higherKinds",
                      "-language:implicitConversions",
                      "-language:postfixOps",
                      "-deprecation",
                      "-unchecked"
                    )

// sbt-release settings

releaseSettings

releaseProcess <<= thisProjectRef apply { ref =>
  Seq[ReleaseStep](
    checkSnapshotDependencies
  , inquireVersions
  , runTest
  , setReleaseVersion
  , commitReleaseVersion
  , tagRelease
  , publishArtifacts
  , setNextVersion
  , commitNextVersion
  , pushChanges
  )
}
