@file:JvmName("HeadlessLauncher")

package dev.ebnbin.insaniquarium.headless

import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import dev.ebnbin.insaniquarium.Insaniquarium

/** Launches the headless application. Can be converted into a server application or a scripting utility. */
fun main() {
    HeadlessApplication(Insaniquarium(), HeadlessApplicationConfiguration().apply {
        // When this value is negative, Insaniquarium#render() is never called:
        updatesPerSecond = -1
    })
}
