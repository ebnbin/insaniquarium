package dev.ebnbin.insaniquarium

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.glutils.HdpiMode
import dev.ebnbin.gdx.utils.fromJson
import dev.ebnbin.gdx.utils.toJson
import java.io.File

fun main() {
    devValidateAssets()

    val listener = LauncherHelper.createGame()
    val config = Lwjgl3ApplicationConfiguration().also {
        it.setMaximized(true)
        it.setHdpiMode(HdpiMode.Pixels)
    }
    Lwjgl3Application(listener, config)
}

private fun devValidateAssets() {
    val configFile = File("../assets/config.json")
    configFile.writeText(configFile.readText().fromJson<Config>().toJson())
}
