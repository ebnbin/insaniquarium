plugins {
    kotlin("jvm")
    application
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(Version.JAVA))

sourceSets.main.configure {
    resources.srcDirs(
        rootProject.file("assets-kgdx"),
        rootProject.file("assets"),
    )
}

application.mainClass.set("dev.ebnbin.insaniquarium.tool.ToolLauncher")

dependencies {
    implementation(project(":core"))
    implementation(Dependency.GDX_BACKEND_HEADLESS)
    implementation(Dependency.GDX_PLATFORM_NATIVES_DESKTOP)
    implementation(Dependency.GDX_TOOLS)
}
