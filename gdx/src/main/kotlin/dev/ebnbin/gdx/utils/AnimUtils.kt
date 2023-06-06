package dev.ebnbin.gdx.utils

fun <T> List<T>.animFrame(
    duration: Float,
    mode: AnimMode,
    interpolation: Interpolation,
    stateTime: Float,
): T {
    require(isNotEmpty())
    if (size == 1) {
        return get(0)
    }

    require(stateTime >= 0f)

    val alpha = when (mode) {
        AnimMode.NORMAL -> {
            (stateTime / duration).minMax(0f, 1f)
        }
        AnimMode.REVERSED -> {
            1f - (stateTime / duration).minMax(0f, 1f)
        }
        AnimMode.LOOP -> {
            (stateTime % duration) / duration
        }
        AnimMode.LOOP_REVERSED -> {
            1f - (stateTime % duration) / duration
        }
    }
    val value = interpolation.apply(alpha)
    val index = (size * value).toInt().minMax(0, size - 1)
    return get(index)
}
