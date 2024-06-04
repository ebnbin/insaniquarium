package dev.ebnbin.kgdx.util

enum class Dpi(
    val id: String,
    val suffix: String,
) {
    M(
        id = "m",
        suffix = "",
    ),
    H(
        id = "h",
        suffix = "-hdpi",
    ),
    XH(
        id = "xh",
        suffix = "-xhdpi",
    ),
    ;

    companion object {
        fun of(id: String): Dpi {
            return entries.single { it.id == id }
        }
    }
}
