package dev.ebnbin.gdx.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

val gson: Gson = GsonBuilder()
    .registerTypeAdapterFactory(EnumTypeAdapterFactory)
    .excludeFieldsWithoutExposeAnnotation()
    .setPrettyPrinting()
    .create()

fun Any?.toJson(): String {
    return gson.toJson(this)
}

fun <T> String.fromJson(classOfT: Class<T>): T {
    return gson.fromJson(this, classOfT)
}

inline fun <reified T> String.fromJson(): T {
    return gson.fromJson(this, object : TypeToken<T>() {}.type)
}

private object EnumTypeAdapterFactory : TypeAdapterFactory {
    override fun <T : Any> create(gson: Gson?, type: TypeToken<T>): TypeAdapter<T>? {
        if (!type.rawType.isEnum) {
            return null
        }
        if (type.rawType.interfaces.none { it == SerializableEnum::class.java }) {
            return null
        }
        val serializedNameMap = type.rawType.enumConstants
            .map {
                @Suppress("UNCHECKED_CAST")
                it as T
            }
            .associateWith { enum ->
                val enumClass = enum::class.java
                enumClass.getField((enum as Enum<*>).name).getAnnotation(SerializedName::class.java)?.value
                    ?: enumClass.getMethod("getSerializedName").also { it.isAccessible = true }.invoke(enum) as String
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
                    val nextString = `in`.nextString()
                    serializedNameMap.entries.single { it.value == nextString }.key
                }
            }
        }
    }
}
