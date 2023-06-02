package dev.ebnbin.gdx

object Gdx {
    fun init(
        gameGetter: () -> BaseGame,
    ) {
        dev.ebnbin.gdx.gameGetter = gameGetter
    }
}
