package dev.ebnbin.gdx.animation

import dev.ebnbin.gdx.utils.SerializableEnum

enum class AnimationMode(override val serializedName: String) : SerializableEnum {
    NORMAL(
        serializedName = "normal",
    ),
    REVERSED(
        serializedName = "reversed",
    ),
    LOOP(
        serializedName = "loop",
    ),
    LOOP_REVERSED(
        serializedName = "loop_reversed",
    ),
    ;
}
