package dev.ebnbin.gdx.utils

enum class AnimMode(
    override val serializedName: String,
) : SerializableEnum {
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
