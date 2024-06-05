package dev.ebnbin.kgdx.util

import dev.ebnbin.kgdx.preference.KgdxPreferenceManager
import kotlin.math.roundToInt

enum class Dpi(
    val id: String,
    val suffix: String,
    val pxsPerDp: Float,
) {
    M(
        id = "m",
        suffix = "",
        pxsPerDp = 1f,
    ),
    H(
        id = "h",
        suffix = "-hdpi",
        pxsPerDp = 1.5f,
    ),
    XH(
        id = "xh",
        suffix = "-xhdpi",
        pxsPerDp = 2f,
    ),
    ;

    val dpsPerPx: Float
        get() = 1f / pxsPerDp

    companion object {
        fun of(id: String): Dpi {
            return entries.single { it.id == id }
        }
    }
}

val Float.dpToPxFloat: Float
    get() = this * KgdxPreferenceManager.dpi.value.pxsPerDp

val Float.dpToPxRound: Int
    get() = dpToPxFloat.roundToInt()

val Float.dpToPxInt: Int
    get() = dpToPxFloat.toInt()
