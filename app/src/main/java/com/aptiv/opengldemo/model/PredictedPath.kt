package com.aptiv.adas.core.model

import com.aptiv.adas.core.model.common.Curve

data class PredictedPath(
    val curve: Curve,
    val laneWidth: Float,
    val isAvailable: Boolean
)
