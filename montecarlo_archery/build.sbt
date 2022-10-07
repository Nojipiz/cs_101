val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "montecarlo_archery",
    mainClass := Some("view.StartView"),
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.scalanlp" %% "breeze" % "2.1.0",
      "org.scalafx" %% "scalafx" % "18.0.2-R29"
    ),
    fork := true,
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", _*) => MergeStrategy.discard
      case _                        => MergeStrategy.first
    }
  )
