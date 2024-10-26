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

application.mainClass.set("dev.ebnbin.insaniquarium.desktop.DesktopLauncher")

dependencies {
    implementation(project(":core"))
    implementation(Dependency.GDX_BACKEND_LWJGL3)
    implementation(Dependency.GDX_PLATFORM_NATIVES_DESKTOP)
    implementation(Dependency.GDX_BOX2D_PLATFORM_NATIVES_DESKTOP)
    implementation(Dependency.GDX_FREETYPE_PLATFORM_NATIVES_DESKTOP)
}

tasks.run.configure {
    if (System.getProperty("os.name").contains("mac", ignoreCase = true)) {
        jvmArgs("-XstartOnFirstThread")
    }
}

// java -XstartOnFirstThread -jar desktop/build/libs/desktop.jar
tasks.jar.configure {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    manifest.attributes("Main-Class" to application.mainClass.get())
}
