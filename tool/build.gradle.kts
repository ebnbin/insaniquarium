plugins {
    kotlin("jvm")
    application
}

java.toolchain.languageVersion = JavaLanguageVersion.of(Version.JAVA)

application.mainClass = "dev.ebnbin.insaniquarium.tool.ToolLauncher"

dependencies {
    implementation(project(":core"))
    implementation(Dependency.GDX_BACKEND_HEADLESS)
    implementation(Dependency.GDX_PLATFORM_NATIVES_DESKTOP)
}
