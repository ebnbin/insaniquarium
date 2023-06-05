package dev.ebnbin.gdx.utils

enum class Interpolation(
    override val serializedName: String,
    private val interpolation: com.badlogic.gdx.math.Interpolation,
) : SerializableEnum {
    LINEAR(
        serializedName = "linear",
        interpolation = com.badlogic.gdx.math.Interpolation.linear,
    ),
    ;

    fun apply(a: Float): Float {
        return interpolation.apply(a)
    }
}
