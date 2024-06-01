package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.Gdx
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
}
