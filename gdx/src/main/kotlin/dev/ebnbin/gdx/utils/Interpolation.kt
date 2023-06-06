package dev.ebnbin.gdx.utils

enum class Interpolation(
    override val serializedName: String,
    private val interpolation: com.badlogic.gdx.math.Interpolation,
) : SerializableEnum {
    LINEAR(
        serializedName = "linear",
        interpolation = com.badlogic.gdx.math.Interpolation.linear,
    ),
    POW2_IN(
        serializedName = "pow2_in",
        interpolation = com.badlogic.gdx.math.Interpolation.pow2In,
    ),
    ;

    fun apply(alpha: Float): Float {
        return interpolation.apply(alpha)
    }
}
