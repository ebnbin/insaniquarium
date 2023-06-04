package dev.ebnbin.gdx.pref

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import dev.ebnbin.gdx.id

abstract class PrefManager(val name: String) {
    val preferences: Preferences
        get() = Gdx.app.getPreferences("$id.$name")
}
