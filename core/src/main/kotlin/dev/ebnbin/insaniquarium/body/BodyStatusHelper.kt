package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Point

object BodyStatusHelper {
    fun nextStatus(
        data: BodyData,
        bodyManager: BodyManager,
        input: BodyInput,
        touchPoint: Point?,
    ): BodyStatus {
        val nextBox = data.box.nextStatus(
            delta = input.delta
        )

        val (nextLife, lifeTmp) = data.life.nextStatus(
            bodyManager = bodyManager,
            input = input,
            touchPoint = touchPoint,
        )

        val nextRenderer = data.renderer.nextStatus(
            delta = input.delta,
            eatenFoodRelation = lifeTmp.foodRelation,
        )

        return BodyStatus(
            box = nextBox,
            life = nextLife,
            renderer = nextRenderer,
        )
    }
}
