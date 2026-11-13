plugins {
    id("kotlin-application-convention")
}

dependencies {
    implementation(projects.core)
}

application {
    mainClass.set("tech.ixor.kotlox.examples.astprinter.MainKt")
}
