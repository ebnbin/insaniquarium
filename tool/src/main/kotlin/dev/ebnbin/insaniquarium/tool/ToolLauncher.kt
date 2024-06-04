@file:JvmName("ToolLauncher")

package dev.ebnbin.insaniquarium.tool

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import dev.ebnbin.insaniquarium.Insaniquarium
import dev.ebnbin.insaniquarium.tool.asset.AssetProcessor
import dev.ebnbin.insaniquarium.tool.asset.KgdxAssetProcessor

fun main() {
    runHeadlessApplication {
        Insaniquarium
        KgdxAssetProcessor.process()
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
