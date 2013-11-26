import ohnosequences.sbt._

Nice.scalaProject

name := "saws"

homepage := Some(url("https://github.com/ohnosequences/saws"))

organization := "ohnosequences"

organizationHomepage := Some(url("http://ohnosequences.com"))

licenses += "AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.txt")

scalaVersion := "2.10.3"

bucketSuffix := "era7.com"


libraryDependencies ++= Seq (
  "com.chuusai" % "shapeless_2.10.2" % "2.0.0-M1",
  "org.scalatest" %% "scalatest" % "1.9.2" % "test"
  )

resolvers ++= Seq(
    "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/",
    "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
  )

