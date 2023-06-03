package dev.ebnbin.gdx.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.lifecycle.baseGame

sealed class Asset<T>(
    @Expose
    val name: String = "",
    @Expose
    val extension: String = "",
) {
    abstract val directory: String

    abstract val type: Class<T>

    abstract val params: AssetLoaderParameters<T>

    private fun assetDescriptor(): AssetDescriptor<T> {
        val file = Gdx.files.internal("$directory/$name${if (extension == "") "" else ".$extension"}")
        return AssetDescriptor(file, type, params)
    }

    fun get(): T {
        val assetDescriptor = assetDescriptor()
        return if (baseGame.assetHelper.isLoaded(assetDescriptor)) {
            baseGame.assetHelper.get(assetDescriptor)
        } else {
            baseGame.assetHelper.load(assetDescriptor)
            baseGame.assetHelper.finishLoadingAsset(assetDescriptor)
        }
    }

    fun unload() {
        val assetDescriptor = assetDescriptor()
        if (!baseGame.assetHelper.isLoaded(assetDescriptor)) {
            return
        }
        baseGame.assetHelper.unload(assetDescriptor.fileName)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Asset<*>
        if (directory != other.directory) return false
        if (name != other.name) return false
        return extension == other.extension
    }

    override fun hashCode(): Int {
        var result = directory.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + extension.hashCode()
        return result
    }
}
