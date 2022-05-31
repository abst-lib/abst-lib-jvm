plugins {
    kotlin("jvm") apply true
    id("org.jetbrains.dokka") apply true
}

dependencies {
    api(project(":io-core"))
}