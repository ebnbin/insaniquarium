@file:JvmName("ToolLauncher")

package dev.ebnbin.insaniquarium.tool

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import dev.ebnbin.insaniquarium.tool.asset.AssetProcessor

fun main() {
    runHeadlessApplication {
        AssetProcessor.process()
    }
}

private fun runHeadlessApplication(action: () -> Unit) {
    val listener = object : ApplicationListener {
        override fun create() {
            action()
        }

        override fun resize(width: Int, height: Int) {
        }

        override fun render() {
        }

        override fun pause() {
        }

        override fun resume() {
        }

        override fun dispose() {
        }
    }
    val config = HeadlessApplicationConfiguration().also {
        it.updatesPerSecond = -1
    }
    HeadlessApplication(listener, config)
}
