package dev.ebnbin.kgdx.scene

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.utils.Disposable
import ktx.assets.disposeSafely

interface LifecycleScene : InputProcessor, Disposable {
    fun resize(width: Int, height: Int)

    fun resume()

    fun render(delta: Float)

    fun pause()

    companion object {
        internal fun List<LifecycleScene>.registerInputProcessor(inputMultiplexer: InputMultiplexer) {
            reversed().forEach { scene ->
                inputMultiplexer.addProcessor(scene)
            }
        }

        internal fun List<LifecycleScene>.resize(width: Int, height: Int) {
            forEach { scene ->
                scene.resize(width, height)
            }
        }

        internal fun List<LifecycleScene>.resume() {
            forEach { scene ->
                scene.resume()
            }
        }

        internal fun List<LifecycleScene>.render(delta: Float) {
            forEach { scene ->
                scene.render(delta)
            }
        }

        internal fun List<LifecycleScene>.pause() {
            reversed().forEach { scene ->
                scene.pause()
            }
        }

        internal fun List<LifecycleScene>.unregisterInputProcessor(inputMultiplexer: InputMultiplexer) {
            reversed().forEach { scene ->
                inputMultiplexer.removeProcessor(scene)
            }
        }

        internal fun List<LifecycleScene>.dispose() {
            reversed().forEach { scene ->
                scene.disposeSafely()
            }
        }
    }
}
