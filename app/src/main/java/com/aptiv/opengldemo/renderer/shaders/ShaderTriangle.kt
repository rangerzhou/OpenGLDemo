package com.aptiv.opengldemo.renderer.shaders

import android.content.res.Resources
import com.aptiv.opengldemo.R

internal class ShaderTriangle(resources: Resources) :
    Shader(R.raw.triangle_vert, R.raw.triangle_fragment, resources) {

    val positionHandle = getAttribLocation("aPosition")
    val mColorHandle = getAttribLocation("aColor")
    val vPMatrixHandle = getUniformLocation("uMVPMatrix")

}