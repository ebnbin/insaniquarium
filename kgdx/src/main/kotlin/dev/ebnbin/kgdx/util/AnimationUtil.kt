package dev.ebnbin.kgdx.util

enum class AnimationMode {
    NORMAL,
    REVERSED,
    LOOP,
    LOOP_REVERSED,
    LOOP_PINGPONG,
    ;
}

fun <T> List<T>.animationFrame(
    duration: Float,
    mode: AnimationMode,
    stateTime: Float,
): T {
    require(isNotEmpty())
    if (size == 1) {
        return first()
    }
    val value = when (mode) {
        AnimationMode.NORMAL -> {
            (stateTime / duration).coerceIn(0f, 1f)
        }
        AnimationMode.REVERSED -> {
            (1f - stateTime / duration).coerceIn(0f, 1f)
        }
        AnimationMode.LOOP -> {
            ((stateTime % duration) + duration) % duration / duration
        }
        AnimationMode.LOOP_REVERSED -> {
            1f - ((stateTime % duration) + duration) % duration / duration
        }
        AnimationMode.LOOP_PINGPONG -> {
            val t = ((stateTime % (duration * 2f)) + duration * 2f) % (duration * 2f) / duration
            if (t <= 1f) {
                t
            } else {
                2f - t
            }
        }
    }
    val index = (size * value).toInt().coerceIn(0, size - 1)
    return get(index)
}
