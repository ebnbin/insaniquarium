package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.insaniquarium.body.BodyActor
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
                valueList = BodyType.PET_LIST.subList(0, BodyType.PET_LIST.size / 2),
                valueToText = { it.id },
                valueToImage = { AnimationImage(textureAsset = it.def.textureAsset) },
            ) {
                devBodyType = it
            }
            listMenuItem(
                menuBar = menuBar,
                text = "body type pet B",
                valueList = BodyType.PET_LIST.subList(BodyType.PET_LIST.size / 2, BodyType.PET_LIST.size),
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

    private var devSelectedBody: BodyActor? = null

    fun isSelected(body: BodyActor): Boolean {
        return devSelectedBody === body
    }

    fun selectBody(body: BodyActor) {
        unselectBody(null)
        devSelectedBody = body
        body.devSelect()
    }

    fun unselectBody(body: BodyActor?) {
        if (body == null) {
            devSelectedBody?.devUnselect()
            devSelectedBody = null
        } else {
            if (devSelectedBody === body) {
                body.devUnselect()
                devSelectedBody = null
            }
        }
    }

    fun createBody(
        type: BodyType?,
        count: Int,
        x: Float? = null,
        y: Float? = null,
    ) {
        repeat(count) {
            BodyActor(
                tank = tank,
                type = type ?: BodyType.entries.random(),
                position = BodyPosition(
                    x = x ?: (Random.nextFloat() * tank.groupWrapper.width),
                    y = y ?: (Random.nextFloat() * tank.groupWrapper.height),
                ),
            ).also {
                tank.groupWrapper.addActor(it)
            }
        }
    }

    fun clearBodies() {
        tank.groupWrapper.clearChildren()
    }

    private val shapeRendererHelper: ShapeRendererHelper = ShapeRendererHelper()

    fun draw(batch: Batch, parentAlpha: Float) {
        shapeRendererHelper.draw(batch) {
            rect(tank.groupWrapper.x, tank.groupWrapper.y, tank.groupWrapper.width, tank.groupWrapper.height)
        }
    }

    fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        val devBodyType = devBodyType
        if (devBodyType != null) {
            createBody(
                type = devBodyType,
                count = 1,
                x = x,
                y = y,
            )
        }
        return true
    }
}
