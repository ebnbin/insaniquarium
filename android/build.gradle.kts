@file:Suppress("UnstableApiUsage")

plugins {
    kotlin("android")
    id("com.android.application")
}

val natives: Configuration by configurations.creating

dependencies {
    implementation(project(":core"))
    implementation(Dependencies.GDX_BACKEND_ANDROID)
    natives(Dependencies.GDX_PLATFORM_NATIVES_ARMEABI_V7A)
    natives(Dependencies.GDX_PLATFORM_NATIVES_ARM64_V8A)
    natives(Dependencies.GDX_PLATFORM_NATIVES_X86)
    natives(Dependencies.GDX_PLATFORM_NATIVES_X86_64)
    natives(Dependencies.GDX_FREETYPE_PLATFORM_NATIVES_ARMEABI_V7A)
    natives(Dependencies.GDX_FREETYPE_PLATFORM_NATIVES_ARM64_V8A)
    natives(Dependencies.GDX_FREETYPE_PLATFORM_NATIVES_X86)
    natives(Dependencies.GDX_FREETYPE_PLATFORM_NATIVES_X86_64)
}

android {
    namespace = "dev.ebnbin.insaniquarium"
    compileSdk = 33 // https://developer.android.com/about/versions
    sourceSets.getByName("main").assets.srcDirs(
        "../assets_gdx",
        "../assets",
    )
    packagingOptions.resources.excludes.add("META-INF/robovm/ios/robovm.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 33
        val version = version.toString()
        versionCode = version.split(".").fold(0) { acc, s -> acc * 100 + s.toInt() }
        versionName = version
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    kotlinOptions.jvmTarget = "1.8"
}

// called every time gradle gets executed, takes the native dependencies of the natives configuration, and extracts
// them to the proper jniLibs/ folders so they get packed with the APK.
val copyAndroidNatives = tasks.register("copyAndroidNatives") {
    doFirst {
        val abiList = listOf(
            "armeabi-v7a",
            "arm64-v8a",
            "x86",
            "x86_64",
        )
        natives.files
            .filter { file ->
                abiList.any { abi ->
                    file.name.endsWith("natives-$abi.jar")
                }
            }
            .forEach { jar ->
                val abi = jar.nameWithoutExtension.substringAfterLast("natives-")
                copy {
                    from(zipTree(jar))
                    into(file("src/main/jniLibs/$abi"))
                    include("*.so")
                }
            }
    }
}

tasks
    .matching { it.name.contains("merge") && it.name.contains("JniLibFolders") }
    .configureEach { dependsOn(copyAndroidNatives) }
