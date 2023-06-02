buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        google()
    }
    dependencies {
        classpath(Dependencies.KOTLIN_GRADLE_PLUGIN)
        classpath(Dependencies.GRADLE)
        classpath(Dependencies.ROBOVM_GRADLE_PLUGIN)
    }
}

allprojects {
    version = "0.0.1"
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/releases/")
        maven("https://jitpack.io")
    }
}
