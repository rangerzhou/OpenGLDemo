package com.aptiv.opengldemo.renderer.objects

import android.content.res.Resources
import com.aptiv.opengldemo.R
import com.aptiv.opengldemo.renderer.MeshCache
import com.aptiv.opengldemo.renderer.ThemeConfigurator
import com.aptiv.opengldemo.renderer.core.Colors.toVec3
import com.aptiv.opengldemo.renderer.meshes.MeshObj
import com.aptiv.opengldemo.renderer.shaders.ShaderObject
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import kotlin.math.PI

internal class HostCar(resources: Resources, meshCache: MeshCache) {
    companion object {
        private const val X_ANGLE = PI.toFloat() / 2.0f
        private const val Z_ANGLE = 20.0f

        private const val BLINKER_INTERVAL_MS = 1000L
        private val BLINKER_SIZE = Vec3(0.7f, 0.35f, 1.0f)
        private val BLINKER_POS_RIGHT = Vec3(0.23f, 0.83f, 4.64f)
        private val BLINKER_POS_LEFT = Vec3(-0.9f, 0.82f, 4.3f)

        private val PLANE_POS = Vec3(-0.35f, 0.85f, 4.5f)
        private val PLANE_SIZE = Vec3(0.7f, 0.2f, 1.0f)

    }

    // host car
    private val shaderCar = ShaderObject(resources)
    private var meshCar = MeshObj(meshCache.get(R.raw.hostcar_v2))

    init {
        // host car
        shaderCar.setObjectToWorld(Mat4())
    }

    fun draw(worldToScreen: Mat4, worldToView: Mat4) {
        // Car 汽车
        shaderCar.use()
        shaderCar.setWorldToView(worldToView)
        shaderCar.setWorldToScreen(worldToScreen)
        /*shaderCar.setViewMatrix()
        shaderCar.setMVPMatrix()*/
        shaderCar.setColor(ThemeConfigurator.theme.hostCarColor.toVec3(), 1.0f)
        meshCar.drawOnce(shaderCar.attribPosition, shaderCar.attribNormal)
    }
}