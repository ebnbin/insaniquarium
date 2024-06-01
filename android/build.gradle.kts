plugins {
    id("com.android.application")
    kotlin("android")
}

java.toolchain.languageVersion = JavaLanguageVersion.of(Version.JAVA)

android {
    namespace = "dev.ebnbin.insaniquarium"
    compileSdk = 34
    sourceSets.named("main").configure {
        assets.srcDirs(rootProject.file("assets"))
    }
    defaultConfig {
        minSdk = 19
        targetSdk = 34
        versionCode = Version.VERSION_CODE
        versionName = Version.VERSION
    }
}

repositories {
    google()
}

val natives: Configuration by configurations.creating

dependencies {
    implementation(project(":core"))
    implementation(Dependency.GDX_BACKEND_ANDROID)
    natives(Dependency.GDX_PLATFORM_NATIVES_ARMEABI_V7A)
    natives(Dependency.GDX_PLATFORM_NATIVES_ARM64_V8A)
    natives(Dependency.GDX_PLATFORM_NATIVES_X86)
    natives(Dependency.GDX_PLATFORM_NATIVES_X86_64)
    natives(Dependency.GDX_FREETYPE_PLATFORM_NATIVES_ARMEABI_V7A)
    natives(Dependency.GDX_FREETYPE_PLATFORM_NATIVES_ARM64_V8A)
    natives(Dependency.GDX_FREETYPE_PLATFORM_NATIVES_X86)
    natives(Dependency.GDX_FREETYPE_PLATFORM_NATIVES_X86_64)
}

tasks.matching { it.name.startsWith("merge") && it.name.endsWith("JniLibFolders") }.configureEach {
    doFirst {
        natives.files.forEach { file ->
            val abi = file.nameWithoutExtension.substringAfterLast("natives-")
            require(abi in setOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
            copy {
                from(zipTree(file))
                into("src/main/jniLibs/$abi")
                include("*.so")
            }
        }
    }
}
