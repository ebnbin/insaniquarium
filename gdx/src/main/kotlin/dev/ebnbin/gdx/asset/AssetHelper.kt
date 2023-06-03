package dev.ebnbin.gdx.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import dev.ebnbin.gdx.utils.diff

class AssetHelper(assets: Assets) : AssetManager() {
    init {
        setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(fileHandleResolver))
        setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(fileHandleResolver))
    }

    val assetSet: Set<Asset<*>> = assets.all()

    private val assetDescriptorMap: Map<Asset<*>, AssetDescriptor<*>> = assetSet.associateWith {
        it.createAssetDescriptor()
    }

    private fun <T> assetDescriptor(asset: Asset<T>): AssetDescriptor<T> {
        @Suppress("UNCHECKED_CAST")
        return assetDescriptorMap.getValue(asset) as AssetDescriptor<T>
    }

    init {
        assetSet
            .filter { it.preload == true }
            .forEach {
                load(it)
                finishLoadingAsset(assetDescriptor(it))
            }
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

    fun loadAllDiff(oldAssetSet: Set<Asset<*>>, newAssetSet: Set<Asset<*>>) {
        val (removed, added) = oldAssetSet.diff(newAssetSet)
        removed.forEach { unload(it) }
        added.forEach { load(it) }
    }
}
