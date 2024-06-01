plugins {
    kotlin("jvm")
    id("robovm")
}

java.toolchain.languageVersion = JavaLanguageVersion.of(Version.JAVA)

dependencies {
    implementation(project(":core"))
    implementation(Dependency.ROBOVM_RT)
    implementation(Dependency.ROBOVM_COCOATOUCH)
    implementation(Dependency.GDX_BACKEND_ROBOVM)
    implementation(Dependency.GDX_PLATFORM_NATIVES_IOS)
    implementation(Dependency.GDX_BOX2D_PLATFORM_NATIVES_IOS)
    implementation(Dependency.GDX_FREETYPE_PLATFORM_NATIVES_IOS)
}
