package com.badlogic.gdx.assets

import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import ktx.collections.GdxArray

open class GdxAssetManager(
    resolver: FileHandleResolver = InternalFileHandleResolver(),
    defaultLoaders: Boolean = true,
) : AssetManager(resolver, defaultLoaders) {
    val publicLoadQueue: GdxArray<AssetDescriptor<*>>
        get() = loadQueue
}
