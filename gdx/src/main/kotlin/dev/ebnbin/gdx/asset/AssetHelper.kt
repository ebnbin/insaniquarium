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

    /**
     * Asset -> AssetDescriptor.
     */
    private val assetDescriptorMap: Map<Asset<*>, AssetDescriptor<*>> = assetSet.associateWith {
        it.createAssetDescriptor()
    }

    private fun <T> assetDescriptor(asset: Asset<T>): AssetDescriptor<T> {
        @Suppress("UNCHECKED_CAST")
        return assetDescriptorMap.getValue(asset) as AssetDescriptor<T>
    }

    /**
     * fileName -> Asset.
     */
    private val assetMap: Map<String, Asset<*>> = assetSet.associateBy {
        assetDescriptor(it).fileName
    }

    init {
        assetSet
            .filter { it.preload }
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

    //*****************************************************************************************************************

    override fun unload(fileName: String?) {
        diffAssets { super.unload(fileName) }
    }

    override fun <T : Any?> addAsset(fileName: String?, type: Class<T>?, asset: T) {
        diffAssets { super.addAsset(fileName, type, asset) }
    }

    override fun clear() {
        diffAssets { super.clear() }
    }

    private fun diffAssets(callSuper: () -> Unit) {
        val oldAssetSet = assetNames.mapNotNullTo(mutableSetOf()) { assetMap[it] }
        callSuper()
        val newAssetSet = assetNames.mapNotNullTo(mutableSetOf()) { assetMap[it] }
        val (removed, added) = oldAssetSet.diff(newAssetSet)
        removed.forEach {
            removeAssetExtraMap(it)
            it.unloaded(this)
        }
        added.forEach {
            it.loaded(this)
        }
    }

    //*****************************************************************************************************************

    private val assetExtraMapMap: MutableMap<Asset<*>, MutableMap<String, Any?>> = mutableMapOf()

    fun <T> getAssetExtraOrPut(asset: Asset<*>, key: String, defaultValue: (AssetHelper) -> T): T {
        if (assetExtraMapMap.containsKey(asset)) {
            val assetExtraMap = assetExtraMapMap.getValue(asset)
            if (assetExtraMap.containsKey(key)) {
                @Suppress("UNCHECKED_CAST")
                return assetExtraMap.getValue(key) as T
            }
        }
        val assetExtraMap = assetExtraMapMap.getOrElse(asset) { mutableMapOf() }
        val assetExtra = defaultValue(this)
        assetExtraMap[key] = assetExtra
        assetExtraMapMap[asset] = assetExtraMap
        return assetExtra
    }

    private fun removeAssetExtraMap(asset: Asset<*>) {
        assetExtraMapMap.remove(asset)
    }

    //*****************************************************************************************************************

    override fun dispose() {
        assetExtraMapMap.clear()
        super.dispose()
    }
}
