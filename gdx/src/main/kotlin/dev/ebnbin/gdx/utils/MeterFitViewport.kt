package dev.ebnbin.gdx.utils

import com.badlogic.gdx.utils.viewport.FitViewport

open class MeterFitViewport : FitViewport(World.unitWidth.unitToMeter, World.unitHeight.unitToMeter)
