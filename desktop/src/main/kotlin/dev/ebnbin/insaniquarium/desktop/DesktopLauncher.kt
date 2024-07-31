@file:JvmName("DesktopLauncher")

package dev.ebnbin.insaniquarium.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.glutils.HdpiMode
import demo.ashley.AshleyGame

fun main() {
    val listener = AshleyGame()
    val config = Lwjgl3ApplicationConfiguration().also {
//        it.setMaximized(true)
        it.setWindowedMode(640, 480)
        it.setHdpiMode(HdpiMode.Pixels)
    }
    Lwjgl3Application(listener, config)
}
