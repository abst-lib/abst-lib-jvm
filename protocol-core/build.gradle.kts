plugins {
    kotlin("jvm") apply true
    id("org.jetbrains.dokka") apply true
}

dependencies {
    api(project(":io-core"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
