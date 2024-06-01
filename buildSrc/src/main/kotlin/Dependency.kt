object Dependency {
    private object Version {
        // https://github.com/libgdx/libgdx
        const val GDX = "1.12.1"

        // https://github.com/MobiVM/robovm
        const val ROBOVM = "2.3.21"

        // https://github.com/libktx/ktx
        const val KTX = "1.12.1-rc1"
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

    // https://github.com/libktx/ktx
    const val KTX_ACTORS = "io.github.libktx:ktx-actors:${Version.KTX}"
    const val KTX_AI = "io.github.libktx:ktx-ai:${Version.KTX}"
    const val KTX_APP = "io.github.libktx:ktx-app:${Version.KTX}"
    const val KTX_ASHLEY = "io.github.libktx:ktx-ashley:${Version.KTX}"
    const val KTX_ASSETS = "io.github.libktx:ktx-assets:${Version.KTX}"
    const val KTX_ASSETS_ASYNC = "io.github.libktx:ktx-assets-async:${Version.KTX}"
    const val KTX_ASYNC = "io.github.libktx:ktx-async:${Version.KTX}"
    const val KTX_BOX2D = "io.github.libktx:ktx-box2d:${Version.KTX}"
    const val KTX_COLLECTIONS = "io.github.libktx:ktx-collections:${Version.KTX}"
    const val KTX_FREETYPE = "io.github.libktx:ktx-freetype:${Version.KTX}"
    const val KTX_FREETYPE_ASYNC = "io.github.libktx:ktx-freetype-async:${Version.KTX}"
    const val KTX_GRAPHICS = "io.github.libktx:ktx-graphics:${Version.KTX}"
    const val KTX_I18N = "io.github.libktx:ktx-i18n:${Version.KTX}"
    const val KTX_INJECT = "io.github.libktx:ktx-inject:${Version.KTX}"
    const val KTX_JSON = "io.github.libktx:ktx-json:${Version.KTX}"
    const val KTX_LOG = "io.github.libktx:ktx-log:${Version.KTX}"
    const val KTX_MATH = "io.github.libktx:ktx-math:${Version.KTX}"
    const val KTX_PREFERENCES = "io.github.libktx:ktx-preferences:${Version.KTX}"
    const val KTX_REFLECT = "io.github.libktx:ktx-reflect:${Version.KTX}"
    const val KTX_SCENE2D = "io.github.libktx:ktx-scene2d:${Version.KTX}"
    const val KTX_STYLE = "io.github.libktx:ktx-style:${Version.KTX}"
    const val KTX_TILED = "io.github.libktx:ktx-tiled:${Version.KTX}"
    const val KTX_VIS = "io.github.libktx:ktx-vis:${Version.KTX}"
    const val KTX_VIS_STYLE = "io.github.libktx:ktx-vis-style:${Version.KTX}"
}
