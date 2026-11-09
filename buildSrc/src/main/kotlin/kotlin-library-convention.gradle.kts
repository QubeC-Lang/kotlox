plugins {
    id("kotlin-jvm-convention")
    id("detekt-convention")
    id("test-logger-convention")
    `java-library`
}

kotlin {
    explicitApi()
}
