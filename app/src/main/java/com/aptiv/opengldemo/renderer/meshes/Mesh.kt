package com.aptiv.opengldemo.renderer.meshes

import glm_.vec3.Vec3

internal interface Mesh {

    companion object {
        const val NOT_USED = -1
    }

    fun destroy()
    fun drawBegin(aPosition: Int, aNormal: Int = NOT_USED, aUV: Int = NOT_USED)
    fun draw()
    fun drawEnd()

    fun getBBMin(): Vec3
    fun getBBMax(): Vec3
}
