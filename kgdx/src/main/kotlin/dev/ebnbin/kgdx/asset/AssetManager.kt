package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import dev.ebnbin.kgdx.util.fromJson
import ktx.freetype.registerFreeTypeFontLoaders

typealias GdxAssetManager = com.badlogic.gdx.assets.AssetManager

internal class AssetManager : GdxAssetManager() {
    init {
        registerFreeTypeFontLoaders()
    }

    val assets: Assets = listOf("kgdx_assets.json", "assets.json")
        .map { Gdx.files.internal(it) }
        .filter { it.exists() }
        .map { it.readString().fromJson(Assets::class.java) }
        .fold(Assets()) { acc, assets -> acc + assets }

    private val assetSet: Set<Asset<*>> = assets.all()

    private val assetDescriptorMap: Map<Asset<*>, AssetDescriptor<*>> = assetSet.associateWith { asset ->
        asset.createAssetDescriptor()
    }

    private fun <T> assetDescriptor(asset: Asset<T>): AssetDescriptor<T> {
        @Suppress("UNCHECKED_CAST")
        return assetDescriptorMap.getValue(asset) as AssetDescriptor<T>
    }

    private val fileNameMap: Map<String, Asset<*>> = assetSet.associateBy { asset ->
        assetDescriptor(asset).fileName
    }

    fun <T> get(asset: Asset<T>): T {
        return get(assetDescriptor(asset))
    }

    override fun unload(fileName: String?) {
        diffAssets {
            super.unload(fileName)
        }
    }

    override fun <T : Any?> addAsset(fileName: String?, type: Class<T>?, asset: T) {
        diffAssets {
            super.addAsset(fileName, type, asset)
        }
    }

    override fun clear() {
        diffAssets {
            super.clear()
        }
    }

    private val loadedAssetSet: MutableSet<Asset<*>> = mutableSetOf()

    private fun diffAssets(updateAssets: () -> Unit) {
        val oldAssetSet = assetNames.mapNotNullTo(mutableSetOf()) { fileNameMap[it] }
        updateAssets()
        val newAssetSet = assetNames.mapNotNullTo(mutableSetOf()) { fileNameMap[it] }
        val removedAssetSet = oldAssetSet - newAssetSet
        val addedAssetSet = newAssetSet - oldAssetSet
        removedAssetSet.forEach { asset ->
            if (!loadedAssetSet.contains(asset)) return@forEach
            loadedAssetSet.remove(asset)
            asset.unloaded()
        }
        addedAssetSet.forEach { asset ->
            if (loadedAssetSet.contains(asset)) return@forEach
            loadedAssetSet.add(asset)
            @Suppress("UNCHECKED_CAST")
            asset as Asset<Any>
            asset.loaded(get(asset))
        }
    }
}
