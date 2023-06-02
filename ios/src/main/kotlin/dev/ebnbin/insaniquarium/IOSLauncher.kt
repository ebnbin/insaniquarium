package dev.ebnbin.insaniquarium

import com.badlogic.gdx.backends.iosrobovm.IOSApplication
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration
import com.badlogic.gdx.graphics.glutils.HdpiMode
import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.uikit.UIApplication

class IOSLauncher : IOSApplication.Delegate() {
    override fun createApplication(): IOSApplication {
        val listener = LauncherHelper.createGame()
        val config = IOSApplicationConfiguration().also {
            it.hdpiMode = HdpiMode.Pixels
        }
        return IOSApplication(listener, config)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            NSAutoreleasePool().use {
                UIApplication.main(args, null as Class<UIApplication>?, IOSLauncher::class.java)
            }
        }
    }
}
