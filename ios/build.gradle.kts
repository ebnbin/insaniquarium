plugins {
    kotlin("jvm")
    id("robovm")
}

dependencies {
    implementation(project(":core"))
    implementation(Dependencies.ROBOVM_RT)
    implementation(Dependencies.ROBOVM_COCOATOUCH)
    implementation(Dependencies.GDX_BACKEND_ROBOVM)
    implementation(Dependencies.GDX_PLATFORM_NATIVES_IOS)
    implementation(Dependencies.GDX_FREETYPE_PLATFORM_NATIVES_IOS)
}
