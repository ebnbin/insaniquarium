package dev.ebnbin.gdx.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import dev.ebnbin.gdx.lifecycle.baseGame

sealed class Asset<T>(
    val name: String,
) {
    abstract fun directory(): String

    abstract fun type(): Class<T>

    abstract fun params(): AssetLoaderParameters<T>

    private fun assetDescriptor(): AssetDescriptor<T> {
        return AssetDescriptor(Gdx.files.internal("${directory()}/$name"), type(), params())
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
        if (directory() != other.directory()) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        var result = directory().hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
