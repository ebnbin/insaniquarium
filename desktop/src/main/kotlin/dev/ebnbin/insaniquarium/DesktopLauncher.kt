package dev.ebnbin.insaniquarium

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.glutils.HdpiMode

fun main() {
    val listener = LauncherHelper.createGame()
    val config = Lwjgl3ApplicationConfiguration().also {
        it.setMaximized(true)
        it.setHdpiMode(HdpiMode.Pixels)
    }
    Lwjgl3Application(listener, config)
}
