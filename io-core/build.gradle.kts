plugins {
    kotlin("jvm") apply true
    id("org.jetbrains.dokka") apply true
}

val msgpackVersion: String by rootProject

dependencies {
    api("org.msgpack:msgpack-core:$msgpackVersion")
}