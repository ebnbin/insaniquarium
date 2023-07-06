package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Point

object BodyStatusHelper {
    fun nextTick(
        data: BodyData,
        delegate: BodyDelegate,
        input: BodyInput,
        touchPoint: Point?,
    ): BodyStatus {
        val (nextLife, lifeTmp) = data.life.nextStatus(
            delegate = delegate,
            input = input,
            touchPoint = touchPoint,
        )

        val nextRenderer = data.renderer.nextStatus(
            input = input,
            eatenFoodRelation = lifeTmp.foodRelation,
        )

        return BodyStatus(
            box = data.box.status,
            life = nextLife,
            renderer = nextRenderer,
        )
    }

    fun nextStatus(
        data: BodyData,
        delta: Float,
    ): BodyStatus {
        val nextBox = data.box.nextStatus(
            delta = delta,
        )

        return BodyStatus(
            box = nextBox,
            life = data.life.status,
            renderer = data.renderer.status,
        )
    }
}
