package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dev.ebnbin.kgdx.game

sealed class Asset<T>(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("extension")
    val extension: String,
    @Expose
    @SerializedName("file_type")
    val fileType: AssetFileType,
    @Expose
    @SerializedName("preload")
    val preload: Boolean,
) {
    abstract val type: AssetType

    abstract val parameters: AssetLoaderParameters<T>

    internal fun createAssetDescriptor(): AssetDescriptor<T> {
        val assetId = AssetId(
            fileType = fileType,
            type = type,
            nameWithExtension = "$name.$extension",
        )
        return AssetDescriptor(assetId.id, type.type(), parameters)
    }

    fun get(): T {
        return game.assetManager.get(this)
    }

    internal open fun loaded(asset: T) {
    }

    internal open fun unloaded() {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Asset<*>
        if (type != other.type) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
