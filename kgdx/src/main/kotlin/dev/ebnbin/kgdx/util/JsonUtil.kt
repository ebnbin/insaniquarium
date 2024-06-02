package dev.ebnbin.kgdx.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

private val gson: Gson = GsonBuilder()
    .excludeFieldsWithoutExposeAnnotation()
    .serializeNulls()
    .setPrettyPrinting()
    .registerTypeAdapterFactory(EnumTypeAdapterFactory)
    .create()

fun Any?.toJson(): String {
    return gson.toJson(this)
}

fun <T> String.fromJson(classOfT: Class<T>): T {
    return gson.fromJson(this, classOfT)
}

interface SerializableEnum {
    val serializedName: String
}

private object EnumTypeAdapterFactory : TypeAdapterFactory {
    override fun <T : Any> create(gson: Gson?, type: TypeToken<T>): TypeAdapter<T>? {
        if (!type.rawType.isEnum) return null
        if (type.rawType.interfaces.none { it === SerializableEnum::class.java }) return null
        val enumMap: MutableMap<String, T> = mutableMapOf()
        val serializedNameMap: MutableMap<T, String> = mutableMapOf()
        type.rawType.enumConstants.forEach { enum ->
            @Suppress("UNCHECKED_CAST")
            enum as T
            val serializedName = enum::class.java.getMethod("getSerializedName").invoke(enum) as String
            enumMap[serializedName] = enum
            serializedNameMap[enum] = serializedName
        }
        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T?) {
                if (value == null) {
                    out.nullValue()
                } else {
                    out.value(serializedNameMap.getValue(value))
                }
            }

            override fun read(`in`: JsonReader): T? {
                return if (`in`.peek() == JsonToken.NULL) {
                    `in`.nextNull()
                    null
                } else {
                    enumMap.getValue(`in`.nextString())
                }
            }
        }
    }
}
