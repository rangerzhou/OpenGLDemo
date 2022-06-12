package com.aptiv.opengldemo.renderer.shaders

import android.content.res.Resources
import android.opengl.GLES31
import com.aptiv.opengldemo.R
import com.aptiv.opengldemo.renderer.core.MatrixState
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3

class ShaderObject(resource: Resources) : Shader(
    R.raw.object_vert,
    R.raw.object_frag, resource) {

    val attribPosition = getAttribLocation("aPosition")
    val attribNormal = getAttribLocation("aNormal")

    private val uColor = getUniformLocation("uColor")
    private val uWorldToScreen = getUniformLocation("uWorldToScreen")
    private val uWorldToView = getUniformLocation("uWorldToView")
    private val uObjectToWorld = getUniformLocation("uObjectToWorld")

    fun setColor(color: Vec3, alpha: Float) {
        GLES31.glProgramUniform4f(program, uColor, color.r, color.g, color.b, alpha)
    }

    fun setColor(r: Float, g: Float, b: Float, a: Float) {
        GLES31.glProgramUniform4f(program, uColor, r, g, b, a)
    }

    fun setWorldToScreen(worldToScreen: Mat4) { // 将总变换矩阵传入渲染管线
        GLES31.glProgramUniformMatrix4fv(program, uWorldToScreen, 1, false, worldToScreen.array, 0)
    }
    fun setMVPMatrix(){
        GLES31.glUniformMatrix4fv(uWorldToScreen,1,false, MatrixState.mvpMatrix,0)
    }

    fun setWorldToView(worldToView: Mat4) { // 将相机视图矩阵传入渲染管线
        GLES31.glProgramUniformMatrix4fv(program, uWorldToView, 1, false, worldToView.array, 0)
    }
    fun setViewMatrix(){
        GLES31.glUniformMatrix4fv(uWorldToView,1,false, MatrixState.viewMatrix,0)
    }

    fun setObjectToWorld(objectToWorld: Mat4) {
        GLES31.glProgramUniformMatrix4fv(program, uObjectToWorld, 1, false, objectToWorld.array, 0)
    }

}
