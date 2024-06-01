package dev.ebnbin.kgdx.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

private val gson: Gson = GsonBuilder()
    .excludeFieldsWithoutExposeAnnotation()
    .serializeNulls()
    .setPrettyPrinting()
    .create()

fun Any?.toJson(): String {
    return gson.toJson(this)
}

fun <T> String.fromJson(classOfT: Class<T>): T {
    return gson.fromJson(this, classOfT)
}
