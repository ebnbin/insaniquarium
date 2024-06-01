object Dependency {
    private object Version {
        // https://github.com/libgdx/libgdx
        const val GDX = "1.12.1"

        // https://github.com/MobiVM/robovm
        const val ROBOVM = "2.3.21"
    }

    // https://github.com/JetBrains/kotlin
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22"

    // https://github.com/libgdx/libgdx
    const val GDX = "com.badlogicgames.gdx:gdx:${Version.GDX}"
    const val GDX_BACKEND_LWJGL3 = "com.badlogicgames.gdx:gdx-backend-lwjgl3:${Version.GDX}"
    const val GDX_BACKEND_ANDROID = "com.badlogicgames.gdx:gdx-backend-android:${Version.GDX}"
    const val GDX_BACKEND_ROBOVM = "com.badlogicgames.gdx:gdx-backend-robovm:${Version.GDX}"
    const val GDX_BACKEND_HEADLESS = "com.badlogicgames.gdx:gdx-backend-headless:${Version.GDX}"
    const val GDX_PLATFORM_NATIVES_DESKTOP = "com.badlogicgames.gdx:gdx-platform:${Version.GDX}:natives-desktop"
    const val GDX_PLATFORM_NATIVES_ARMEABI_V7A = "com.badlogicgames.gdx:gdx-platform:${Version.GDX}:natives-armeabi-v7a"
    const val GDX_PLATFORM_NATIVES_ARM64_V8A = "com.badlogicgames.gdx:gdx-platform:${Version.GDX}:natives-arm64-v8a"
    const val GDX_PLATFORM_NATIVES_X86 = "com.badlogicgames.gdx:gdx-platform:${Version.GDX}:natives-x86"
    const val GDX_PLATFORM_NATIVES_X86_64 = "com.badlogicgames.gdx:gdx-platform:${Version.GDX}:natives-x86_64"
    const val GDX_PLATFORM_NATIVES_IOS = "com.badlogicgames.gdx:gdx-platform:${Version.GDX}:natives-ios"
    const val GDX_BOX2D = "com.badlogicgames.gdx:gdx-box2d:${Version.GDX}"
    const val GDX_BOX2D_PLATFORM_NATIVES_DESKTOP = "com.badlogicgames.gdx:gdx-box2d-platform:${Version.GDX}:natives-desktop"
    const val GDX_BOX2D_PLATFORM_NATIVES_ARMEABI_V7A = "com.badlogicgames.gdx:gdx-box2d-platform:${Version.GDX}:natives-armeabi-v7a"
    const val GDX_BOX2D_PLATFORM_NATIVES_ARM64_V8A = "com.badlogicgames.gdx:gdx-box2d-platform:${Version.GDX}:natives-arm64-v8a"
    const val GDX_BOX2D_PLATFORM_NATIVES_X86 = "com.badlogicgames.gdx:gdx-box2d-platform:${Version.GDX}:natives-x86"
    const val GDX_BOX2D_PLATFORM_NATIVES_X86_64 = "com.badlogicgames.gdx:gdx-box2d-platform:${Version.GDX}:natives-x86_64"
    const val GDX_BOX2D_PLATFORM_NATIVES_IOS = "com.badlogicgames.gdx:gdx-box2d-platform:${Version.GDX}:natives-ios"
    const val GDX_FREETYPE = "com.badlogicgames.gdx:gdx-freetype:${Version.GDX}"
    const val GDX_FREETYPE_PLATFORM_NATIVES_DESKTOP = "com.badlogicgames.gdx:gdx-freetype-platform:${Version.GDX}:natives-desktop"
    const val GDX_FREETYPE_PLATFORM_NATIVES_ARMEABI_V7A = "com.badlogicgames.gdx:gdx-freetype-platform:${Version.GDX}:natives-armeabi-v7a"
    const val GDX_FREETYPE_PLATFORM_NATIVES_ARM64_V8A = "com.badlogicgames.gdx:gdx-freetype-platform:${Version.GDX}:natives-arm64-v8a"
    const val GDX_FREETYPE_PLATFORM_NATIVES_X86 = "com.badlogicgames.gdx:gdx-freetype-platform:${Version.GDX}:natives-x86"
    const val GDX_FREETYPE_PLATFORM_NATIVES_X86_64 = "com.badlogicgames.gdx:gdx-freetype-platform:${Version.GDX}:natives-x86_64"
    const val GDX_FREETYPE_PLATFORM_NATIVES_IOS = "com.badlogicgames.gdx:gdx-freetype-platform:${Version.GDX}:natives-ios"
    const val GDX_TOOLS = "com.badlogicgames.gdx:gdx-tools:${Version.GDX}"

    // https://maven.google.com/web/index.html
    const val GRADLE = "com.android.tools.build:gradle:8.2.2"

    // https://github.com/MobiVM/robovm
    const val ROBOVM_GRADLE_PLUGIN = "com.mobidevelop.robovm:robovm-gradle-plugin:${Version.ROBOVM}"
    const val ROBOVM_RT = "com.mobidevelop.robovm:robovm-rt:${Version.ROBOVM}"
    const val ROBOVM_COCOATOUCH = "com.mobidevelop.robovm:robovm-cocoatouch:${Version.ROBOVM}"

    // https://github.com/libgdx/gdx-ai
    const val GDX_AI = "com.badlogicgames.gdx:gdx-ai:1.8.2"

    // https://github.com/libgdx/ashley
    const val ASHLEY = "com.badlogicgames.ashley:ashley:1.7.4"
}
