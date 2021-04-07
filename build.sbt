import org.jetbrains.sbtidea.Keys._

lazy val sbt_idea_stateful_comp =
  project.in(file("."))
    .enablePlugins(SbtIdeaPlugin)
    .settings(
      version := "0.8.2",
      scalaVersion := "2.13.5",
      ThisBuild / intellijPluginName := "Stateful Component",
      ThisBuild / intellijBuild      := "203.7717.56",
      ThisBuild / intellijPlatform   := IntelliJPlatform.IdeaCommunity,
      Global    / intellijAttachSources := true,
      Compile / javacOptions ++= "--release" :: "11" :: Nil,
      intellijPlugins ++= Seq("com.intellij.properties".toPlugin, "org.intellij.scala:2020.3.23".toPlugin),
      libraryDependencies += "com.eclipsesource.minimal-json" % "minimal-json" % "0.9.5" withSources(),
      unmanagedResourceDirectories in Compile += baseDirectory.value / "resources",
      unmanagedResourceDirectories in Test    += baseDirectory.value / "testResources",
    )
