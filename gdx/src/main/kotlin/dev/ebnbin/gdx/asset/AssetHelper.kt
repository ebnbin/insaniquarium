package dev.ebnbin.gdx.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager

class AssetHelper(assets: Assets) : AssetManager() {
    val assetSet: Set<Asset<*>> = assets.all()

    private val assetDescriptorMap: Map<Asset<*>, AssetDescriptor<*>> = assetSet.associateWith {
        it.createAssetDescriptor()
    }

    private fun <T> assetDescriptor(asset: Asset<T>): AssetDescriptor<T> {
        @Suppress("UNCHECKED_CAST")
        return assetDescriptorMap.getValue(asset) as AssetDescriptor<T>
    }

    fun <T> load(asset: Asset<T>) {
        load(assetDescriptor(asset))
    }

    fun <T> get(asset: Asset<T>): T {
        return get(assetDescriptor(asset))
    }

    fun <T> unload(asset: Asset<T>) {
        val assetDescriptor = assetDescriptor(asset)
        if (!isLoaded(assetDescriptor)) {
            return
        }
        unload(assetDescriptor.fileName)
    }
}
