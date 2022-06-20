package com.aptiv.adas.core.model.common

data class Curve(
    val a0: Float,
    val a1: Float,
    val a2: Float,
    val a3: Float,
    val startDistance: Float,
    val endDistance: Float
)
