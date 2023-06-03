plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(Dependencies.GDX_TOOLS)
}

fun configureRunTask(task: JavaExec, debug: Boolean = false) {
    task.apply {
        dependsOn(tasks.classes)
        mainClass.set("MainKt")
        classpath(sourceSets.main.get().runtimeClasspath)
        standardInput = System.`in`
        this.debug = debug
    }
}

tasks.register<JavaExec>("run") {
    configureRunTask(this)
}

tasks.register<JavaExec>("debug") {
    configureRunTask(this, debug = true)
}
