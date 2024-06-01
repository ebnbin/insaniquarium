package dev.ebnbin.kgdx.asset

import ktx.freetype.registerFreeTypeFontLoaders

typealias GdxAssetManager = com.badlogic.gdx.assets.AssetManager

internal class AssetManager : GdxAssetManager() {
    init {
        registerFreeTypeFontLoaders()
    }
}
