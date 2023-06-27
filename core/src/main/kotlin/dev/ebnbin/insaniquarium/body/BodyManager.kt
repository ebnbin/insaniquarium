package dev.ebnbin.insaniquarium.body

class BodyManager(
    private val body: Body,
) {
    fun findBodyByType(typeSet: Set<BodyType>): List<Body> {
        return body.tank.findBodyByType(typeSet)
    }
}
