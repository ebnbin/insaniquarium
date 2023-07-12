package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.Position
import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.body.Body
import dev.ebnbin.insaniquarium.body.BodyActor
import dev.ebnbin.insaniquarium.body.BodyGroup
import dev.ebnbin.insaniquarium.body.BodyState
import dev.ebnbin.insaniquarium.body.BodyType
import java.util.UUID

class Tank : Group() {
    private val groupMap: Map<BodyGroup, Group> = BodyGroup.values().associateWith { Group().also { addActor(it) } }
    private val idMap: MutableMap<String, BodyActor> = mutableMapOf()
    private val typeMap: MutableMap<BodyType, MutableList<BodyActor>> = BodyType.values().associateWithTo(mutableMapOf()) {
        mutableListOf()
    }

    init {
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                val touchPoint = Point(x, y)
                this@Tank.touchPoint = touchPoint
                if (hitMoneyBodies(touchPoint)) {
                    return true
                }
                devSelectedBodyType?.let {
                    addBody(
                        type = it,
                        state = BodyState(
                            position = Position(
                                x = x,
                                y = y,
                            ),
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

    private fun hitMoneyBodies(touchPoint: Point): Boolean {
        val moneyGroup = groupMap.getValue(BodyGroup.MONEY)
        moneyGroup.children
            .reversed()
            .forEach {
                require(it is BodyActor)
                if (it.body.touch(touchPoint)) {
                    return true
                }
            }
        return false
    }

    //*****************************************************************************************************************

    fun addBody(
        type: BodyType,
        id: String = "${UUID.randomUUID()}",
        state: BodyState = BodyState(),
        index: Int? = null,
    ): Body {
        val bodyActor = BodyActor(
            tank = this,
            type = type,
            id = id,
            state = state,
        )
        val group = groupMap.getValue(bodyActor.body.config.group)
        if (index == null) {
            group.addActor(bodyActor)
        } else {
            group.addActorAt(index, bodyActor)
        }
        idMap[bodyActor.id] = bodyActor
        typeMap.getValue(type).add(bodyActor)
        return bodyActor.body
    }

    fun removeBody(body: Body): Int {
        val bodyActor = idMap.getValue(body.id)
        typeMap.getValue(bodyActor.type).remove(bodyActor)
        idMap.remove(bodyActor.id)
        val group = groupMap.getValue(bodyActor.body.config.group)
        val index = group.children.indexOf(bodyActor, true)
        if (index != -1) {
            group.removeActorAt(index, true)
        }
        return index
    }

    fun replaceBody(
        oldBody: Body,
        type: BodyType,
        state: BodyState,
    ): Body {
        val index = removeBody(oldBody)
        return addBody(
            type = type,
            id = oldBody.id,
            state = state,
            index = index
        )
    }

    fun findBodyByType(typeSet: Set<BodyType>): List<Body> {
        return typeSet.flatMap { typeMap.getValue(it) }.map { it.body }
    }

    //*****************************************************************************************************************

    var devSelectedBodyType: BodyType? = null

    fun devAddBody() {
        val type = devSelectedBodyType ?: BodyType.values().random()
        addBody(
            type = type,
            state = BodyState(
                position = Position(
                    x = Random.nextFloat(0f, width),
                    y = Random.nextFloat(0f, height),
                ),
            ),
        )
    }

    fun devClearBodies() {
        typeMap.values.reversed().forEach { it.clear() }
        idMap.clear()
        groupMap.values.reversed().forEach { it.clearChildren() }
    }

    fun tick(delta: Float) {
        children.forEach { group ->
            group as Group
            group.children.forEach { body ->
                body as BodyActor
                body.tick(delta)
            }
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        baseGame.putLog("tank") {
            groupMap.entries.joinToString(separator = ",") { (group, groupActor) ->
                "${group.serializedName}:${groupActor.children.size}"
            }
        }
        baseGame.putLog("selectedBodyType") {
            "$devSelectedBodyType"
        }
    }
}
