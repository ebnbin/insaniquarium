plugins {
    kotlin("jvm")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(Version.JAVA))

dependencies {
    api(project(":kgdx"))
}
