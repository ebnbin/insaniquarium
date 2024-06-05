package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import dev.ebnbin.kgdx.game
import dev.ebnbin.kgdx.util.fromJson
import dev.ebnbin.kgdx.util.internalAsset
import ktx.assets.setLoader
import ktx.assets.unloadSafely
import ktx.freetype.registerFreeTypeFontLoaders

typealias GdxAssetManager = com.badlogic.gdx.assets.AssetManager

internal class AssetManager : GdxAssetManager(AssetFileHandleResolver), AssetLoaderRegistry {
    init {
        registerFreeTypeFontLoaders()
        setLoader(JsonAsset.Loader(fileHandleResolver), ".json")
        game.registerAssetLoaders(this, fileHandleResolver)
    }

    val assets: Assets = listOf("kgdx_assets.json", "assets.json")
        .map { Gdx.files.internalAsset(it) }
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

    private val loadedAssetSet: MutableSet<Asset<*>> = mutableSetOf()

    init {
        assetSet
            .filter { it.preload }
            .forEach { asset ->
                load(asset)
                finishLoadingAsset(assetDescriptor(asset))
            }
    }

    fun <T> get(asset: Asset<T>): T {
        return get(assetDescriptor(asset))
    }

    fun <T> load(asset: Asset<T>) {
        load(assetDescriptor(asset))
    }

    fun <T> unload(asset: Asset<T>) {
        unloadSafely(assetDescriptor(asset).fileName)
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

    private fun diffAssets(updateAssets: () -> Unit) {
        val oldAssetSet = assetNames.mapNotNullTo(mutableSetOf()) { fileNameMap[it] }
        updateAssets()
        val newAssetSet = assetNames.mapNotNullTo(mutableSetOf()) { fileNameMap[it] }
        val removedAssetSet = oldAssetSet - newAssetSet
        val addedAssetSet = newAssetSet - oldAssetSet
        removedAssetSet.forEach { asset ->
            if (!loadedAssetSet.contains(asset)) return@forEach
            assetExtraMapMap.remove(asset)
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

    private val assetExtraMapMap: MutableMap<Asset<*>, MutableMap<String, Any?>> = mutableMapOf()

    internal fun <T, U> getAssetExtraOrPut(asset: Asset<T>, key: String, defaultValue: (T) -> U): U {
        if (assetExtraMapMap.containsKey(asset)) {
            val assetExtraMap = assetExtraMapMap.getValue(asset)
            if (assetExtraMap.containsKey(key)) {
                @Suppress("UNCHECKED_CAST")
                return assetExtraMap.getValue(key) as U
            }
        }
        val assetExtraMap = assetExtraMapMap.getOrElse(asset) { mutableMapOf() }
        val assetExtra = defaultValue(get(asset))
        assetExtraMap[key] = assetExtra
        assetExtraMapMap[asset] = assetExtraMap
        return assetExtra
    }
}

private object AssetFileHandleResolver : FileHandleResolver {
    override fun resolve(fileName: String): FileHandle {
        return AssetId.of(fileName).fileHandle()
    }
}
