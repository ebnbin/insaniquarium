package dev.ebnbin.insaniquarium.body

data class BodyStatus(
    val box: BodyBox.Status = BodyBox.Status(),
    val life: BodyLife.Status = BodyLife.Status(),
    val renderer: BodyRenderer.Status = BodyRenderer.Status(),
)
