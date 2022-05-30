plugins {
    kotlin("jvm") version "1.6.20" apply false
    id("org.jetbrains.dokka") version "1.6.21" apply false
}

allprojects {
    group = "io.github.nickacpt.abst-lib"

    val version: String by rootProject
    this.version = version

    repositories {
        mavenCentral()
    }
}