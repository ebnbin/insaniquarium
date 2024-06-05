package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dev.ebnbin.kgdx.util.fromJson

class Json(val data: Any?)

class JsonAsset(
    name: String,
    extension: String,
    fileType: AssetFileType,
    preload: Boolean,
    @Expose
    @SerializedName("class")
    val classOfT: Class<*>,
) : Asset<Json>(
    name = name,
    extension = extension,
    fileType = fileType,
    preload = preload,
) {
    override val type: AssetType
        get() = AssetType.JSON

    override val parameters: AssetLoaderParameters<Json>
        get() = Parameters(classOfT)

    fun <T> data(): T {
        @Suppress("UNCHECKED_CAST")
        return get().data as T
    }

    class Parameters(val classOfT: Class<*>) : AssetLoaderParameters<Json>()

    class Loader(resolver: FileHandleResolver) : AsynchronousAssetLoader<Json, Parameters>(resolver) {
        override fun getDependencies(
            fileName: String?,
            file: FileHandle?,
            parameter: Parameters?,
        ): Array<AssetDescriptor<Any>>? {
            return null
        }

        private var json: Json? = null

        override fun loadAsync(manager: AssetManager?, fileName: String?, file: FileHandle, parameter: Parameters?) {
            requireNotNull(parameter)
            val data = file.readString().fromJson(parameter.classOfT)
            json = Json(data)
        }

        override fun loadSync(
            manager: AssetManager?,
            fileName: String?,
            file: FileHandle?,
            parameter: Parameters?
        ): Json {
            return requireNotNull(json).also {
                json = null
            }
        }
    }
}
