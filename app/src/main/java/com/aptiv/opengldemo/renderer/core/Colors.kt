package com.aptiv.opengldemo.renderer.core

import android.graphics.Color
import glm_.vec3.Vec3
import glm_.vec4.Vec4

internal object Colors {
    val WHITE = Vec4(1.0f, 1.0f, 1.0f, 1.0f)
    val ORANGE = Vec4(1.0f, 0.7f, 0.0f, 1.0f)
    val RED = Vec4(1.0f, 0.0f, 0.0f, 1.0f)
    val GREEN = Vec4(0.0f, 1.0f, 0.0f, 1.0f)
    val YELLOW = Vec4(1.0f, 1.0f, 0.0f, 1.0f)
    val BLUE = Vec4(0.0f, 0.0f, 1.0f, 1.0f)

    fun String.toVec4(): Vec4 {
        val color = Color.parseColor(this)
        return Vec4(
            Color.red(color) / 255.0f,
            Color.green(color) / 255.0f,
            Color.blue(color) / 255.0f,
            Color.alpha(color) / 255.0f)
    }

    fun String.toVec3(): Vec3 {
        val color = Color.parseColor(this)
        return Vec3(
            Color.red(color) / 255.0f,
            Color.green(color) / 255.0f,
            Color.blue(color) / 255.0f)
    }
}
