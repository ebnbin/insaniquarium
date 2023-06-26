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
    private val foodGroup: Group = Group()
    private val fishGroup: Group = Group()
    private val petGroup: Group = Group()
    private val moneyGroup: Group = Group()

    init {
        addActor(foodGroup)
        addActor(fishGroup)
        addActor(petGroup)
        addActor(moneyGroup)

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
        moneyGroup.children
            .filterIsInstance<Body>()
            .reversed()
            .forEach {
                if (it.data.hit(touchPoint)) {
                    return true
                }
            }
        return false
    }

    //*****************************************************************************************************************

    private val bodyMap: MutableMap<String, Body> = mutableMapOf()

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
        getGroup(body.config.group).addActor(body)
        bodyMap[body.id] = body
        return body
    }

    fun removeBody(body: Body) {
        bodyMap.remove(body.id)
        getGroup(body.config.group).removeActor(body)
    }

    private fun getGroup(group: BodyGroup): Group {
        return when (group) {
            BodyGroup.FOOD -> foodGroup
            BodyGroup.FISH -> fishGroup
            BodyGroup.PET -> petGroup
            BodyGroup.MONEY -> moneyGroup
        }
    }

    fun findBodyByType(typeSet: Set<BodyType>): List<Body> {
        return bodyMap.values
            .filter { typeSet.contains(it.type) }
    }

    fun findBodyById(id: String): Body? {
        return bodyMap[id]
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
        bodyMap.clear()
        moneyGroup.clearChildren()
        petGroup.clearChildren()
        fishGroup.clearChildren()
        foodGroup.clearChildren()
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (debug) {
            baseGame.putLog("tank") {
                "food:${foodGroup.children.size}," +
                    "fish:${fishGroup.children.size}," +
                    "pet:${petGroup.children.size}," +
                    "money:${moneyGroup.children.size}"
            }
            baseGame.putLog("selectedBodyType") {
                "$devSelectedBodyType"
            }
        }
    }
}
