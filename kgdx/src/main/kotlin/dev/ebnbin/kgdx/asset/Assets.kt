package dev.ebnbin.kgdx.asset

data class Assets(
    private val freeType: Map<String, FreeTypeAsset>,
) {
    fun freeType(name: String): FreeTypeAsset {
        return freeType.getValue(name)
    }
}
