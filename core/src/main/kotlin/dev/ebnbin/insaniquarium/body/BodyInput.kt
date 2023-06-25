package dev.ebnbin.insaniquarium.body

data class BodyInput(
    val delta: Float = 0f,
    val damage: Float = 0f,
    val exhaustion: Float = 0f,
) {
    val skipUpdate: Boolean = delta == 0f && damage == 0f && exhaustion == 0f
}
