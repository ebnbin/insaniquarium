package dev.ebnbin.insaniquarium.body

class BodyManager(
    private val body: Body,
) {
    fun findBodyByType(typeSet: Set<BodyType>): List<Body> {
        return body.tank.findBodyByType(typeSet)
    }

    fun findNearestBodyByType(typeSet: Set<BodyType>): Body? {
        val bodies = body.tank.findBodyByType(typeSet)
        return bodies.minByOrNull { body.data.box.distance(it.data.box) }
    }

    fun removeSelf() {
        body.tank.removeBody(body)
    }

    fun addBody(
        type: BodyType,
        createStatus: (body: Body) -> BodyStatus,
        delta: Float,
    ): Body {
        val newBody = body.tank.addBody(
            type = type,
            createStatus = createStatus,
        )
        newBody.act(delta = delta)
        return newBody
    }

    fun replaceBody(
        type: BodyType,
        createStatus: (body: Body) -> BodyStatus,
        delta: Float,
    ): Body {
        val newBody = body.tank.replaceBody(body, type, createStatus)
        newBody.act(delta = delta)
        return newBody
    }

    fun actSelf(input: BodyInput): Body {
        return body.act(input)
    }
}
