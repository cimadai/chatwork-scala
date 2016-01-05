name := "chatwork-scala"

version := "1.0.1"

scalaVersion := "2.11.7"

useGpg in GlobalScope := true

def scalatest = "org.scalatest" % "scalatest_2.11" % "2.2.5" % Test
def dispatch = "net.databinder.dispatch" %% "dispatch-core" % "0.11.3"
def json4s_core = "org.json4s" %% "json4s-core" % "3.2.11"
def json4s_ast = "org.json4s" %% "json4s-ast" % "3.2.11"
def json4s_jackson = "org.json4s" %% "json4s-jackson" % "3.2.11"
def json4s_native = "org.json4s" %% "json4s-native" % "3.2.11"
def jackson_scala = "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.1"

lazy val settings = Seq(
  organization := "net.cimadai",
  scalaVersion := "2.11.7",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-encoding", "UTF-8"),
  javacOptions ++= Seq("-source", "1.8", "-target", "1.7", "-encoding", "UTF-8"),
  javaOptions ++= Seq("-Xmx1G"),
  libraryDependencies ++= Seq(
    dispatch,
    json4s_core, json4s_ast, json4s_jackson, json4s_native, jackson_scala,
    scalatest
  ),
  fork in Test := true,

  publishMavenStyle := true,

  publishArtifact in Test := false,

  pomIncludeRepository := { _ => false },

  publishTo <<= version { (v: String) =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },

  pomExtra := <url>https://github.com/cimadai/chatwork-scala</url>
    <licenses>
      <license>
        <name>The MIT License</name>
        <url>http://www.opensource.org/licenses/mit-license.php</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:cimadai/chatwork-scala.git</url>
      <connection>scm:git:git@github.com:cimadai/chatwork-scala.git</connection>
    </scm>
    <developers>
      <developer>
        <id>cimadai</id>
        <name>Daisuke Shimada</name>
        <url>https://github.com/cimadai</url>
      </developer>
    </developers>
)

lazy val chatworkScala = (project in file("."))
  .settings(settings: _*)
  .settings(name := "chatwork-scala")

