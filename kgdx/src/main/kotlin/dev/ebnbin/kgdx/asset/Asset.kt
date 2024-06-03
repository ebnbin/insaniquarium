package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.util.SerializableEnum

sealed class Asset<T>(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("extension")
    val extension: String,
    @Expose
    @SerializedName("file_type")
    val fileType: FileType,
    @Expose
    @SerializedName("preload")
    val preload: Boolean,
) {
    enum class FileType(override val serializedName: String) : SerializableEnum {
        INTERNAL("internal"),
        LOCAL("local"),
        ;

        companion object {
            fun of(serializedName: String): FileType {
                return entries.first { it.serializedName == serializedName }
            }
        }
    }

    abstract val directory: String

    abstract val type: Class<T>

    abstract val parameters: AssetLoaderParameters<T>

    internal fun createAssetDescriptor(): AssetDescriptor<T> {
        return AssetDescriptor("${fileType.serializedName}:$directory/$name.$extension", type, parameters)
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
        if (directory != other.directory) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = directory.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
