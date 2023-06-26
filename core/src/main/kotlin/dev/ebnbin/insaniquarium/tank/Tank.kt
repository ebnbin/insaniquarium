package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.body.Body
import dev.ebnbin.insaniquarium.body.BodyGroup
import dev.ebnbin.insaniquarium.body.BodyStatus
import dev.ebnbin.insaniquarium.body.BodyType
import java.util.UUID

class Tank : Group() {
    private val groupMap: Map<BodyGroup, Group> = BodyGroup.values().associateWith { Group().also { addActor(it) } }
    private val idMap: MutableMap<String, Body> = mutableMapOf()
    private val typeMap: MutableMap<BodyType, MutableList<Body>> = BodyType.values().associateWithTo(mutableMapOf()) {
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
                        createStatus = {
                            BodyStatus(
                                x = x,
                                y = y,
                            )
                        },
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
                require(it is Body)
                if (it.data.hit(touchPoint)) {
                    return true
                }
            }
        return false
    }

    //*****************************************************************************************************************

    fun addBody(
        type: BodyType,
        createStatus: (body: Body) -> BodyStatus,
    ): Body {
        val body = Body(
            tank = this,
            type = type,
            id = "${UUID.randomUUID()}",
            createStatus = createStatus,
        )
        groupMap.getValue(body.config.group).addActor(body)
        idMap[body.id] = body
        typeMap.getValue(type).add(body)
        return body
    }

    fun removeBody(body: Body) {
        typeMap.getValue(body.type).remove(body)
        idMap.remove(body.id)
        groupMap.getValue(body.config.group).removeActor(body)
    }

    fun findBodyByType(typeSet: Set<BodyType>): List<Body> {
        return typeSet.flatMap { typeMap.getValue(it) }
    }

    fun findBodyById(id: String): Body? {
        return idMap[id]
    }

    //*****************************************************************************************************************

    var devSelectedBodyType: BodyType? = null

    fun devAddBody() {
        val type = devSelectedBodyType ?: BodyType.values().random()
        addBody(
            type = type,
            createStatus = {
                BodyStatus(
                    x = Random.nextFloat(0f, width),
                    y = Random.nextFloat(0f, height),
                )
            },
        )
    }

    fun devClearBodies() {
        typeMap.values.reversed().forEach { it.clear() }
        idMap.clear()
        groupMap.values.reversed().forEach { it.clearChildren() }
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
