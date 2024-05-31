package dev.ebnbin.insaniquarium.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import dev.ebnbin.insaniquarium.LauncherHelper

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listener = LauncherHelper.createGame()
        val config = AndroidApplicationConfiguration()
        initialize(listener, config)
    }
}
