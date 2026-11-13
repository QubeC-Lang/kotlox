rootProject.name = "kotlox"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":core")
include(":repl")
project(":repl").projectDir = file("examples/repl")
include(":ast-printer")
project(":ast-printer").projectDir = file("examples/ast-printer")
include(":generate-ast")
project(":generate-ast").projectDir = file("tools/generate-ast")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("org.jetbrains.kotlinx.kover.aggregation") version "0.9.1"
}
kover {
    enableCoverage()
}
