import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("version-management-convention")
    id("detekt-convention")
}

tasks {
    // detektAll is a task that runs detekt on all src sets
    val detektAll by registering {
        allprojects {
            this@registering.dependsOn(tasks.withType<Detekt>())
        }
    }
    register("build") {
        group = "build"
        dependsOn(detektAll, subprojects.map { "${it.path}:build" })
        finalizedBy(koverXmlReport, koverHtmlReport)
    }
}
