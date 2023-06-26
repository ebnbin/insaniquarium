package dev.ebnbin.insaniquarium.body

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.ebnbin.gdx.lifecycle.baseGame

object BodyDevHelper {
    fun act(data: BodyData) {
        baseGame.putLog("type,id         ") {
            "${data.body.type.serializedName},${data.body.id}"
        }
        data.box.devPutLogs()
        baseGame.putLog("health          ") {
            "${data.status.health?.devText()}"
        }
        baseGame.putLog("hunger          ") {
            "${data.status.hunger?.devText()},${data.hungerStatus}"
        }
        baseGame.putLog("growth          ") {
            "${data.status.growth?.devText()}"
        }
        baseGame.putLog("drop            ") {
            "${data.status.drop?.devText()}"
        }
    }

    fun draw(data: BodyData, shapes: ShapeRenderer) {
        data.box.devDraw(shapes)
    }
}
