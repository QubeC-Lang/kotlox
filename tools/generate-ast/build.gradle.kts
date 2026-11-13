plugins {
    id("kotlin-application-convention")
}

application {
    mainClass.set("tech.ixor.kotlox.tools.GenerateAstKt")
}

tasks.named<JavaExec>("run") {
    // Resolve the output directory argument against the repository root.
    workingDir = rootDir
}
