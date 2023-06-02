object Dependencies {
    // https://github.com/JetBrains/kotlin
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21"

    // https://developer.android.com/build/releases/gradle-plugin
    // https://maven.google.com/web/index.html
    const val GRADLE = "com.android.tools.build:gradle:7.4.2"

    // https://github.com/MobiVM/robovm
    const val ROBOVM_GRADLE_PLUGIN = "com.mobidevelop.robovm:robovm-gradle-plugin:${Versions.ROBOVM}"
    const val ROBOVM_RT = "com.mobidevelop.robovm:robovm-rt:${Versions.ROBOVM}"
    const val ROBOVM_COCOATOUCH = "com.mobidevelop.robovm:robovm-cocoatouch:${Versions.ROBOVM}"
}
