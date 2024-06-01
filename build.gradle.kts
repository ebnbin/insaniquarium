buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(Dependency.KOTLIN_GRADLE_PLUGIN)
        classpath(Dependency.GRADLE)
        classpath(Dependency.ROBOVM_GRADLE_PLUGIN)
    }
}

subprojects {
    repositories {
        mavenCentral()
    }
}
