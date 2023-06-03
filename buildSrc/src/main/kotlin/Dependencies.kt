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

    // https://github.com/libgdx/libgdx
    const val GDX = "com.badlogicgames.gdx:gdx:${Versions.GDX}"
    const val GDX_FREETYPE = "com.badlogicgames.gdx:gdx-freetype:${Versions.GDX}"

    const val GDX_BACKEND_LWJGL3 = "com.badlogicgames.gdx:gdx-backend-lwjgl3:${Versions.GDX}"
    const val GDX_PLATFORM_NATIVES_DESKTOP = "com.badlogicgames.gdx:gdx-platform:${Versions.GDX}:natives-desktop"
    const val GDX_FREETYPE_PLATFORM_NATIVES_DESKTOP = "com.badlogicgames.gdx:gdx-freetype-platform:${Versions.GDX}:natives-desktop"

    const val GDX_BACKEND_ANDROID = "com.badlogicgames.gdx:gdx-backend-android:${Versions.GDX}"
    const val GDX_PLATFORM_NATIVES_ARMEABI_V7A = "com.badlogicgames.gdx:gdx-platform:${Versions.GDX}:natives-armeabi-v7a"
    const val GDX_PLATFORM_NATIVES_ARM64_V8A = "com.badlogicgames.gdx:gdx-platform:${Versions.GDX}:natives-arm64-v8a"
    const val GDX_PLATFORM_NATIVES_X86 = "com.badlogicgames.gdx:gdx-platform:${Versions.GDX}:natives-x86"
    const val GDX_PLATFORM_NATIVES_X86_64 = "com.badlogicgames.gdx:gdx-platform:${Versions.GDX}:natives-x86_64"
    const val GDX_FREETYPE_PLATFORM_NATIVES_ARMEABI_V7A = "com.badlogicgames.gdx:gdx-freetype-platform:${Versions.GDX}:natives-armeabi-v7a"
    const val GDX_FREETYPE_PLATFORM_NATIVES_ARM64_V8A = "com.badlogicgames.gdx:gdx-freetype-platform:${Versions.GDX}:natives-arm64-v8a"
    const val GDX_FREETYPE_PLATFORM_NATIVES_X86 = "com.badlogicgames.gdx:gdx-freetype-platform:${Versions.GDX}:natives-x86"
    const val GDX_FREETYPE_PLATFORM_NATIVES_X86_64 = "com.badlogicgames.gdx:gdx-freetype-platform:${Versions.GDX}:natives-x86_64"

    const val GDX_BACKEND_ROBOVM = "com.badlogicgames.gdx:gdx-backend-robovm:${Versions.GDX}"
    const val GDX_PLATFORM_NATIVES_IOS = "com.badlogicgames.gdx:gdx-platform:${Versions.GDX}:natives-ios"
    const val GDX_FREETYPE_PLATFORM_NATIVES_IOS = "com.badlogicgames.gdx:gdx-freetype-platform:${Versions.GDX}:natives-ios"

    const val GDX_TOOLS = "com.badlogicgames.gdx:gdx-tools:${Versions.GDX}"

    // https://github.com/kotcrab/vis-ui
    const val VIS_UI = "com.kotcrab.vis:vis-ui:1.5.1"

    // https://github.com/google/gson
    const val GSON = "com.google.code.gson:gson:2.10.1"
}
