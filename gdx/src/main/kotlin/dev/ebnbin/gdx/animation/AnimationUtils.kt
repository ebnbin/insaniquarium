package dev.ebnbin.gdx.animation

import dev.ebnbin.gdx.utils.Interpolation

fun <T> List<T>.animFrame(
    duration: Float,
    mode: AnimationMode,
    interpolation: Interpolation,
    stateTime: Float,
): T {
    require(isNotEmpty())
    if (size == 1) {
        return get(0)
    }

    require(stateTime >= 0f)

    val alpha = when (mode) {
        AnimationMode.NORMAL -> {
            (stateTime / duration).coerceIn(0f, 1f)
        }
        AnimationMode.REVERSED -> {
            1f - (stateTime / duration).coerceIn(0f, 1f)
        }
        AnimationMode.LOOP -> {
            (stateTime % duration) / duration
        }
        AnimationMode.LOOP_REVERSED -> {
            1f - (stateTime % duration) / duration
        }
    }
    val value = interpolation.apply(alpha)
    val index = (size * value).toInt().coerceIn(0, size - 1)
    return get(index)
}
