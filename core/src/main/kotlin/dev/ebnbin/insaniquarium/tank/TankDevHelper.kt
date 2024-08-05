package dev.ebnbin.insaniquarium.tank

import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.insaniquarium.body.Body
import dev.ebnbin.insaniquarium.body.BodyPosition
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.preference.PreferenceManager
import dev.ebnbin.kgdx.dev.toDevEntry
import dev.ebnbin.kgdx.ui.AnimationImage
import dev.ebnbin.kgdx.util.checkBoxMenuItem
import dev.ebnbin.kgdx.util.listMenuItem
import dev.ebnbin.kgdx.util.menuItem
import kotlin.random.Random

class TankDevHelper(
    private val tank: Tank,
) {
    fun addedToStage(stage: TankStage) {
        stage.putDevInfo("devBodyType" toDevEntry {
            "${tank.tankComponent.selectedBodyType?.id}"
        })
        stage.putDevInfo("touchPosition" toDevEntry {
            "%.3f,%.3f".format(
                tank.tankComponent.touchPosition?.x ?: Float.NaN,
                tank.tankComponent.touchPosition?.y ?: Float.NaN,
            )
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
                tank.tankComponent.selectedBodyType = it
            }
            listMenuItem(
                menuBar = menuBar,
                text = "body type pet B",
                valueList = BodyType.DEV_PET_LIST_B,
                valueToText = { it.id },
                valueToImage = { AnimationImage(textureAsset = it.def.textureAsset) },
            ) {
                tank.tankComponent.selectedBodyType = it
            }
            menuItem(
                menuBar = menuBar,
                text = "reset body type",
            ) {
                tank.tankComponent.selectedBodyType = null
            }
            listMenuItem(
                menuBar = menuBar,
                text = "create body",
                valueList = listOf(1, 10, 100, 1000),
            ) { count ->
                createBody(
                    type = tank.tankComponent.selectedBodyType,
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
}
