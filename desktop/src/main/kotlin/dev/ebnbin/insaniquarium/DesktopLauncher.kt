package dev.ebnbin.insaniquarium

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    val listener = InsaniquariumGame()
    val config = Lwjgl3ApplicationConfiguration().also {
        it.setTitle("Insaniquarium")
        it.setForegroundFPS(60)
    }
    Lwjgl3Application(listener, config)
}
