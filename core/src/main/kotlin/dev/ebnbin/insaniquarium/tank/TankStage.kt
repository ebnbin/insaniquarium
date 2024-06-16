package dev.ebnbin.insaniquarium.tank

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.kgdx.LifecycleStage
import dev.ebnbin.kgdx.dev.toDevEntry
import dev.ebnbin.kgdx.ui.TextureRegionImage
import dev.ebnbin.kgdx.util.listMenuItem
import dev.ebnbin.kgdx.util.menuItem

class TankStage : LifecycleStage(TankViewport()) {
    private val tankGroup: TankGroup = TankGroup().also {
        addActor(it)
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        tankGroup.setPosition(
            (width - tankGroup.width) / 2f,
            (height - tankGroup.height) / 3f,
            Align.bottomLeft,
        )
    }

    private var accumulator: Float = 0f

    var tickDelta: Float = 0f
        private set

    override fun act(delta: Float) {
        accumulator += delta
        tickDelta = if (accumulator >= TICK_DELTA) {
            accumulator -= TICK_DELTA
            TICK_DELTA
        } else {
            0f
        }
        super.act(delta)
    }

    var devBodyType: BodyType? = null
        private set

    init {
        putDevInfo("devBodyType" toDevEntry {
            "${devBodyType?.id}"
        })
    }

    override val hasDevMenu: Boolean
        get() = true

    override fun createDevMenu(menuBar: MenuBar, menu: Menu) {
        super.createDevMenu(menuBar, menu)
        menu.apply {
            listMenuItem(
                menuBar = menuBar,
                text = "body type pet A",
                valueList = BodyType.PET_LIST.subList(0, BodyType.PET_LIST.size / 2),
                valueToText = { it.id },
                valueToImage = { type ->
                    TextureRegionImage(
                        textureRegionList = type.def.textureAsset.getTextureRegionList(),
                        duration = requireNotNull(type.def.textureAsset.region?.animation?.duration),
                        animationMode = requireNotNull(type.def.textureAsset.region?.animation?.mode),
                    )
                },
            ) {
                devBodyType = it
            }
            listMenuItem(
                menuBar = menuBar,
                text = "body type pet B",
                valueList = BodyType.PET_LIST.subList(BodyType.PET_LIST.size / 2, BodyType.PET_LIST.size),
                valueToText = { it.id },
                valueToImage = { type ->
                    TextureRegionImage(
                        textureRegionList = type.def.textureAsset.getTextureRegionList(),
                        duration = requireNotNull(type.def.textureAsset.region?.animation?.duration),
                        animationMode = requireNotNull(type.def.textureAsset.region?.animation?.mode),
                    )
                },
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
                tankGroup.devCreateBody(
                    type = devBodyType,
                    count = count,
                )
            }
            menuItem(
                menuBar = menuBar,
                text = "clear bodies",
            ) {
                tankGroup.devClearBodies()
            }
        }
    }

    companion object {
        private const val TICKS_PER_SECOND = 20
        private const val TICK_DELTA = 1f / TICKS_PER_SECOND
    }
}
