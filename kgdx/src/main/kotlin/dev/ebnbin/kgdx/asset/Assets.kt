package dev.ebnbin.kgdx.asset

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Assets(
    @Expose
    @SerializedName("free_type")
    private val freeType: Map<String, FreeTypeAsset>,
) {
    fun freeType(name: String): FreeTypeAsset {
        return freeType.getValue(name)
    }
}
