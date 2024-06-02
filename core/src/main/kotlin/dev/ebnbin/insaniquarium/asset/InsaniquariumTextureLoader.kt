package dev.ebnbin.insaniquarium.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.files.FileHandle

class InsaniquariumTextureLoader(resolver: FileHandleResolver) : TextureLoader(resolver) {
    override fun loadAsync(manager: AssetManager?, fileName: String, file: FileHandle?, parameter: TextureParameter?) {
        TextureAssetProcessor.process(fileName)
        super.loadAsync(manager, fileName, file, parameter)
    }
}
