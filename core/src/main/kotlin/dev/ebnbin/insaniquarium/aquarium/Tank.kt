package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.body.Body
import dev.ebnbin.insaniquarium.body.BodyConfig
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.body.BodyParams

class Tank : Group() {
    private val fishGroup: Group = Group()
    private val petGroup: Group = Group()
    private val moneyGroup: Group = Group()

    init {
        addActor(fishGroup)
        addActor(petGroup)
        addActor(moneyGroup)

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                touchPoint = Point(x, y)
                devSelectedBodyType?.let {
                    addBody(
                        params = BodyParams(
                            type = it,
                            x = x,
                            y = y,
                        ),
                    )
                }
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                super.touchUp(event, x, y, pointer, button)
                touchPoint = null
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                super.touchDragged(event, x, y, pointer)
                touchPoint = Point(x, y)
            }
        })

        setSize(960f.unitToMeter, 600f.unitToMeter)
    }

    var touchPoint: Point? = null
        private set

    private val toRemoveBodySet: MutableSet<String> = mutableSetOf()

    private fun isBodyRemoved(bodyId: String): Boolean {
        return toRemoveBodySet.contains(bodyId)
    }

    fun addBodyToRemove(bodyId: String) {
        toRemoveBodySet.add(bodyId)
    }

    private fun addBody(params: BodyParams): Body {
        val body = Body(
            tank = this,
            params = params,
        )
        getGroup(body.data.config.group).addActor(body)
        return body
    }

    private fun removeBody(body: Body) {
        getGroup(body.data.config.group).removeActor(body)
    }

    private fun getGroup(group: BodyConfig.Group): Group {
        return when (group) {
            BodyConfig.Group.FISH -> fishGroup
            BodyConfig.Group.PET -> petGroup
            BodyConfig.Group.MONEY -> moneyGroup
        }
    }

    fun findBodyByType(typeSet: Set<BodyType>): List<Body> {
        return children
            .filterIsInstance<Group>()
            .flatMap { it.children }
            .filterIsInstance<Body>()
            .filter { typeSet.contains(it.data.config.type) }
            .filter { !it.data.canRemove && !isBodyRemoved(it.data.id) }
    }

    //*****************************************************************************************************************

    var devSelectedBodyType: BodyType? = null

    fun devAddBody(type: BodyType = BodyType.values().random()) {
        addBody(
            params = BodyParams(
                type = type,
                x = Random.nextFloat(0f, width),
                y = Random.nextFloat(0f, height),
            ),
        )
    }

    fun devClearBodies() {
        moneyGroup.clearChildren()
        petGroup.clearChildren()
        fishGroup.clearChildren()
    }

    override fun act(delta: Float) {
        super.act(delta)
        children
            .filterIsInstance<Group>()
            .flatMap { it.children }
            .filterIsInstance<Body>()
            .filter { it.data.canRemove || isBodyRemoved(it.data.id) }
            .forEach { removeBody(it) }
        if (debug) {
            baseGame.putLog("tank") {
                "fish:${fishGroup.children.size},pet:${petGroup.children.size},money:${moneyGroup.children.size}"
            }
        }
    }
}
