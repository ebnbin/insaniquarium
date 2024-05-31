@file:JvmName("Lwjgl3Launcher")

package dev.ebnbin.insaniquarium.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import dev.ebnbin.insaniquarium.LauncherHelper

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(LauncherHelper.createGame(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("insaniquarium")
        setWindowedMode(640, 480)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
