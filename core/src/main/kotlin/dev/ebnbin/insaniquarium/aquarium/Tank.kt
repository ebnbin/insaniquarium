package dev.ebnbin.insaniquarium.aquarium

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import dev.ebnbin.gdx.utils.Point
import dev.ebnbin.gdx.utils.Random
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.body.Body
import dev.ebnbin.insaniquarium.body.BodyConfig
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.body.BodyParams

class Tank : Group() {
    private val petGroup: Group = Group()
    private val moneyGroup: Group = Group()

    init {
        addActor(petGroup)
        addActor(moneyGroup)

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                touchPoint = Point(x, y)
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

    private fun addBody(
        type: BodyType,
        params: BodyParams = BodyParams(),
    ): Body {
        val body = Body(
            tank = this,
            type = type,
            params = params,
        )
        getGroup(body.data.config.group).addActor(body)
        return body
    }

    private fun getGroup(group: BodyConfig.Group): Group {
        return when (group) {
            BodyConfig.Group.PET -> petGroup
            BodyConfig.Group.MONEY -> moneyGroup
        }
    }

    fun devAddBody(type: BodyType = BodyType.values().random()) {
        addBody(
            type = type,
            params = BodyParams(
                x = Random.nextFloat(0f, width),
                y = Random.nextFloat(0f, height),
            ),
        )
    }

    fun devClearBodies() {
        moneyGroup.clearChildren()
        petGroup.clearChildren()
    }
}
