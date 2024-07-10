package com.badlogic.gdx.assets

import ktx.collections.GdxArray

val AssetManager.publicLoadQueue: GdxArray<AssetDescriptor<*>>
    get() = loadQueue
