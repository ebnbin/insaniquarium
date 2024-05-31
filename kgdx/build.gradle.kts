plugins {
    kotlin("jvm")
}

java.toolchain.languageVersion = JavaLanguageVersion.of(Version.JAVA)

dependencies {
    api(Dependency.GDX)
    api(Dependency.GDX_FREETYPE)
}
