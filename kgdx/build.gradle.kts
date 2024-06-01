plugins {
    kotlin("jvm")
}

java.toolchain.languageVersion = JavaLanguageVersion.of(Version.JAVA)

dependencies {
    api(Dependency.GDX)
    api(Dependency.GDX_BOX2D)
    api(Dependency.GDX_FREETYPE)
    api(Dependency.GDX_AI)
    api(Dependency.ASHLEY)
}
