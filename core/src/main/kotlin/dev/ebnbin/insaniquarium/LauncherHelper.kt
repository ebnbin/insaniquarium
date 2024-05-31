package dev.ebnbin.insaniquarium

import com.badlogic.gdx.ApplicationListener

object LauncherHelper {
    fun createGame(): ApplicationListener {
        return Insaniquarium()
    }
}
