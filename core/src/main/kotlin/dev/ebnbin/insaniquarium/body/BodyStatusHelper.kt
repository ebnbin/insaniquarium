package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.Point

object BodyStatusHelper {
    fun nextTick(
        data: BodyData,
        delegate: BodyDelegate,
        input: BodyInput,
        touchPoint: Point?,
    ): BodyStatus {
        val nextLife = data.life.nextStatus(
            delegate = delegate,
            input = input,
            touchPoint = touchPoint,
        )

        return BodyStatus(
            box = data.box.status,
            life = nextLife,
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
        )
    }
}
