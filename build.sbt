import ohnosequences.sbt._

Era7.allSettings

name := "saws"

homepage := Some(url("https://github.com/ohnosequences/saws"))

organization := "ohnosequences"

organizationHomepage := Some(url("http://ohnosequences.com"))

licenses += "AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.txt")

scalaVersion := "2.10.3"

bucketSuffix := "era7.com"

libraryDependencies ++= Seq (
    "com.chuusai" %% "shapeless" % "1.2.4"
  , "org.scalatest" %% "scalatest" % "1.9.2" % "test"
  )

scalacOptions ++= Seq(
    "-feature"
  , "-language:higherKinds"
  , "-language:implicitConversions"
  , "-language:postfixOps"
  , "-deprecation"
  , "-unchecked"
  )
