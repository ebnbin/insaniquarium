import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(Dependencies.GDX_BACKEND_LWJGL3)
    implementation(Dependencies.GDX_PLATFORM_NATIVES_DESKTOP)
    implementation(Dependencies.GDX_FREETYPE_PLATFORM_NATIVES_DESKTOP)
}

sourceSets.main.configure {
    resources.srcDirs(
        "../assets_gdx",
        "../assets",
    )
}

val mainClassName = "dev.ebnbin.insaniquarium.DesktopLauncherKt"

fun configureRunTask(task: JavaExec, debug: Boolean = false) {
    task.apply {
        dependsOn(tasks.classes)
        mainClass.set(mainClassName)
        classpath(sourceSets.main.get().runtimeClasspath)
        standardInput = System.`in`
        if (OperatingSystem.current().isMacOsX) {
            jvmArgs("-XstartOnFirstThread") // Required to run on macOS
        }
        this.debug = debug
    }
}

tasks.register<JavaExec>("run") {
    configureRunTask(this)
}

tasks.register<JavaExec>("debug") {
    configureRunTask(this, debug = true)
}

// java -XstartOnFirstThread -jar desktop/build/libs/desktop-${VERSION}.jar
tasks.register<Jar>("dist") {
    dependsOn(tasks.classes, configurations.runtimeClasspath)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest.attributes("Main-Class" to mainClassName)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get())
}
