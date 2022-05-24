package com.aptiv.OpenGLDemo.renderer.shaders

import android.content.res.Resources
import com.aptiv.OpenGLDemo.R
import com.aptiv.OpenGLDemo.renderer.core.Shader

internal class ShaderTriangle(resources: Resources) :
    Shader(R.raw.triangle_vert, R.raw.triangle_fragment, resources) {

    val positionHandle = getAttribLocation("aPosition")
    val mColorHandle = getAttribLocation("aColor")
    val vPMatrixHandle = getUniformLocation("uMVPMatrix")

}