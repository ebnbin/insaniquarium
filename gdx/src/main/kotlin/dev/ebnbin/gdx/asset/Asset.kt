package dev.ebnbin.gdx.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.lifecycle.baseGame

sealed class Asset<T>(
    @Expose
    val name: String,
    @Expose
    val extension: String,
    @Expose
    val preload: Boolean = false,
) {
    abstract val directory: String

    abstract val type: Class<T>

    abstract val params: AssetLoaderParameters<T>

    /**
     * Should only be called by [AssetHelper].
     */
    internal fun createAssetDescriptor(): AssetDescriptor<T> {
        val file = Gdx.files.internal("$directory/$name${if (extension == "") "" else ".$extension"}")
        return AssetDescriptor(file, type, params)
    }

    fun isLoaded(): Boolean {
        return baseGame.assetHelper.isLoaded(this)
    }

    fun get(): T {
        return baseGame.assetHelper.get(this)
    }

    open fun loaded(assetHelper: AssetHelper) {
    }

    open fun unloaded(assetHelper: AssetHelper) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Asset<*>
        if (directory != other.directory) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        var result = directory.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
