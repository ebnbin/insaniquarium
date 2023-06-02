package dev.ebnbin.insaniquarium

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listener = LauncherHelper.createGame()
        val config = AndroidApplicationConfiguration()
        initialize(listener, config)
    }
}
