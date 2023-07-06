package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Point

object BodyStatusHelper {
    fun nextStatus(
        data: BodyData,
        delegate: BodyDelegate,
        delta: Float,
        input: BodyInput,
        touchPoint: Point?,
    ): BodyStatus {
        val nextBox = data.box.nextStatus(
            delta = delta,
        )

        val (nextLife, lifeTmp) = data.life.nextStatus(
            delegate = delegate,
            delta = delta,
            input = input,
            touchPoint = touchPoint,
        )

        val nextRenderer = data.renderer.nextStatus(
            delta = delta,
            input = input,
            eatenFoodRelation = lifeTmp.foodRelation,
        )

        return BodyStatus(
            box = nextBox,
            life = nextLife,
            renderer = nextRenderer,
        )
    }
}
