buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(Dependency.KOTLIN_GRADLE_PLUGIN)
        classpath(Dependency.GRADLE)
    }
}

subprojects {
    repositories {
        mavenCentral()
    }
}
