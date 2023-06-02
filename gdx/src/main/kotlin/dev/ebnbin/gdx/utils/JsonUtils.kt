package dev.ebnbin.gdx.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

val gson: Gson = GsonBuilder()
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
