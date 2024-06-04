package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import dev.ebnbin.kgdx.util.internalAsset
import dev.ebnbin.kgdx.util.localAsset

data class AssetId(
    val fileType: AssetFileType,
    val directory: String,
    val nameWithExtension: String,
) {

    val id: String = "${fileType.id}:$directory:$nameWithExtension"

    fun fileHandle(): FileHandle {
        val path = "$directory/$nameWithExtension"
        return when (fileType) {
            AssetFileType.INTERNAL -> Gdx.files.internalAsset(path)
            AssetFileType.LOCAL -> Gdx.files.localAsset(path)
        }
    }

    companion object {
        fun of(id: String): AssetId {
            val (fileType, directory, nameWithExtension) = id.split(":")
            return AssetId(AssetFileType.of(fileType), directory, nameWithExtension)
        }
    }
}
