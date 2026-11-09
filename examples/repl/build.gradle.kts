plugins {
    id("kotlin-application-convention")
}

dependencies {
    implementation(projects.core)
}

application {
    mainClass.set("tech.ixor.kotlox.examples.repl.MainKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
