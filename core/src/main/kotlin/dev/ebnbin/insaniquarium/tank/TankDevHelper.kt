package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.insaniquarium.body.Body
import dev.ebnbin.insaniquarium.body.BodyPosition
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.preference.PreferenceManager
import dev.ebnbin.kgdx.dev.toDevEntry
import dev.ebnbin.kgdx.ui.AnimationImage
import dev.ebnbin.kgdx.util.ShapeRendererHelper
import dev.ebnbin.kgdx.util.checkBoxMenuItem
import dev.ebnbin.kgdx.util.listMenuItem
import dev.ebnbin.kgdx.util.menuItem
import kotlin.random.Random

class TankDevHelper(
    private val tank: Tank,
) {
    private var devBodyType: BodyType? = null

    fun addedToStage(stage: TankStage) {
        stage.putDevInfo("devBodyType" toDevEntry {
            "${devBodyType?.id}"
        })
        stage.putDevInfo("touchPosition" toDevEntry {
            "${tank.touchPosition}"
        })
    }

    fun removedFromStage(stage: TankStage) {
    }

    val hasDevMenu: Boolean = true

    fun createDevMenu(menuBar: MenuBar, menu: Menu) {
        menu.apply {
            checkBoxMenuItem(
                menuBar = menuBar,
                text = PreferenceManager.enableBodySmoothPosition.key,
                valueProperty = PreferenceManager.enableBodySmoothPosition::value,
            )
            listMenuItem(
                menuBar = menuBar,
                text = "body type pet A",
                valueList = BodyType.DEV_PET_LIST_A,
                valueToText = { it.id },
                valueToImage = { AnimationImage(textureAsset = it.def.textureAsset) },
            ) {
                devBodyType = it
            }
            listMenuItem(
                menuBar = menuBar,
                text = "body type pet B",
                valueList = BodyType.DEV_PET_LIST_B,
                valueToText = { it.id },
                valueToImage = { AnimationImage(textureAsset = it.def.textureAsset) },
            ) {
                devBodyType = it
            }
            menuItem(
                menuBar = menuBar,
                text = "reset body type",
            ) {
                devBodyType = null
            }
            listMenuItem(
                menuBar = menuBar,
                text = "create body",
                valueList = listOf(1, 10, 100, 1000),
            ) { count ->
                createBody(
                    type = devBodyType,
                    count = count,
                )
            }
            menuItem(
                menuBar = menuBar,
                text = "clear bodies",
            ) {
                clearBodies()
            }
        }
    }

    private var devSelectedBody: Body? = null

    fun isSelected(body: Body): Boolean {
        return devSelectedBody === body
    }

    fun selectBody(body: Body) {
        unselectBody(null)
        devSelectedBody = body
        body.devHelper.devSelect()
    }

    fun unselectBody(body: Body?) {
        if (body == null) {
            devSelectedBody?.devHelper?.devUnselect()
            devSelectedBody = null
        } else {
            if (devSelectedBody === body) {
                body.devHelper.devUnselect()
                devSelectedBody = null
            }
        }
    }

    private fun createBody(
        type: BodyType?,
        count: Int,
        x: Float? = null,
        y: Float? = null,
    ) {
        repeat(count) {
            tank.addBody(
                type = type ?: BodyType.entries.random(),
                position = BodyPosition(
                    x = x ?: (Random.nextFloat() * tank.actorWrapper.width),
                    y = y ?: (Random.nextFloat() * tank.actorWrapper.height),
                ),
            )
        }
    }

    private fun clearBodies() {
        tank.clearBodies()
    }

    private val shapeRendererHelper: ShapeRendererHelper = ShapeRendererHelper()

    fun draw(batch: Batch, parentAlpha: Float) {
        shapeRendererHelper.draw(batch = batch) {
            rect(tank.actorWrapper.x, tank.actorWrapper.y, tank.actorWrapper.width, tank.actorWrapper.height)
        }
    }

    fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        val devBodyType = devBodyType ?: return false
        createBody(
            type = devBodyType,
            count = 1,
            x = x,
            y = y,
        )
        return true
    }
}
