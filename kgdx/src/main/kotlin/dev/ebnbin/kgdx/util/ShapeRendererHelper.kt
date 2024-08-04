package dev.ebnbin.kgdx.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType

class ShapeRendererHelper {
    private val shapeRenderer: ShapeRenderer = ShapeRenderer().also {
        it.setAutoShapeType(true)
    }

    fun draw(
        enabled: Boolean = true,
        batch: Batch,
        type: ShapeType = ShapeType.Line,
        action: ShapeRenderer.() -> Unit,
    ) {
        if (!enabled) return
        batch.end()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapeRenderer.projectionMatrix = batch.projectionMatrix
        shapeRenderer.transformMatrix = batch.transformMatrix
        shapeRenderer.begin(type)
        action(shapeRenderer)
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
        batch.begin()
    }
}
