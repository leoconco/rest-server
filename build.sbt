lazy val projectName = "rest-server"

version := "1.0"

lazy val globalSettings = Seq(
  conflictManager := ConflictManager.latestRevision, // this is the default, but we set it explicitly for IDEA. See: https://youtrack.jetbrains.com/issue/SCL-7646
  organization := "com.coloso",
  scalaVersion := "2.11.8",
  updateOptions := updateOptions.value.withCachedResolution(true),
  version := sys.env.get("BUILD_NUMBER") getOrElse s"${System.currentTimeMillis}-SNAPSHOT"
)

lazy val subprojectSettings = Seq(
  libraryDependencies ++= Seq(
    "com.typesafe"             % "config"                    % "1.3.0",
    "org.apache.logging.log4j" % "log4j-slf4j-impl"          % "2.2",
    "org.apache.logging.log4j" % "log4j-api"                 % "2.2",
    "org.apache.logging.log4j" % "log4j-core"                % "2.2",
    "com.google.guava"         % "guava"                     % "19.0",
    "com.google.oauth-client"  % "google-oauth-client-java6" % "1.22.0",
    "com.twitter.finatra"     %% "finatra-http"              % "2.1.6"
      exclude("com.google.guava", "guava")
  ),
  libraryDependencies ++= testLibraries map { _ % Test }
)

lazy val testLibraries = Seq(
  "org.scalatest"  %% "scalatest"                   % "2.2.4",
  "org.scalacheck" %% "scalacheck"                  % "1.12.4",
  "org.scalamock"  %% "scalamock-scalatest-support" % "3.2.2",
  "org.mockito"     % "mockito-all"                 % "1.10.19"
)

lazy val rootSettings = Seq.empty

lazy val serviceSettings = Seq.empty ++
  {
    // Assembly Plugin
    import sbtassembly.AssemblyKeys._
    import sbtassembly.MergeStrategy

    Seq(
      mainClass := Option("com.coloso.ColosoServerMain"),

      assemblyJarName in assembly := s"${projectName}-service.jar",

      test in assembly := {},

      assemblyMergeStrategy in assembly := {
        case PathList("org", "apache", "commons", "logging", _*) => MergeStrategy.first
        case PathList("org", "apache", "log4j", _*) => MergeStrategy.first
        case PathList("org", "slf4j", "impl", _*) => MergeStrategy.first
        case PathList("META-INF", "MANIFEST.MF", _*) => MergeStrategy.discard
        case PathList("META-INF", "mime.types", _*) => MergeStrategy.first
        case "BUILD"  => MergeStrategy.discard
        case s => (assemblyMergeStrategy in assembly).value(s)
      }
    )
  }

lazy val remoteTestsSettings = Seq.empty ++
  {
    // Workaround to make test libraries visible in IDEA
    Seq(libraryDependencies ++= testLibraries)
  }

lazy val End2EndTest = config("e2e") extend Test


lazy val root = Project(id = projectName, base = file("."))
  .aggregate(service, remoteTests)
  .settings(globalSettings: _*)
  .settings(rootSettings: _*)

lazy val service = (project in file("service"))
  .configs(IntegrationTest)
  .settings(globalSettings: _*)
  .settings(subprojectSettings: _*)
  .settings(serviceSettings: _*)

lazy val remoteTests = (project in file("remoteTests"))
  .configs(End2EndTest)
  .settings(globalSettings: _*)
  .settings(subprojectSettings: _*)
  .settings(remoteTestsSettings: _*)

