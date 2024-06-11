package dev.ebnbin.kgdx.util

import com.badlogic.gdx.Gdx

private const val TAG = "kgdx"

fun logD(messageOrException: Any?, tag: String = TAG) {
    if (messageOrException is Throwable) {
        Gdx.app.debug(tag, "", messageOrException)
    } else {
        Gdx.app.debug(tag, "$messageOrException")
    }
}

fun logI(messageOrException: Any?, tag: String = TAG) {
    if (messageOrException is Throwable) {
        Gdx.app.log(tag, "", messageOrException)
    } else {
        Gdx.app.log(tag, "$messageOrException")
    }
}

fun logE(messageOrException: Any?, tag: String = TAG) {
    if (messageOrException is Throwable) {
        Gdx.app.error(tag, "", messageOrException)
    } else {
        Gdx.app.error(tag, "$messageOrException")
    }
}
