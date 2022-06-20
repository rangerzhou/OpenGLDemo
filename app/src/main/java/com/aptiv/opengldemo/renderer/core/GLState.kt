package com.aptiv.opengldemo.renderer.core

import android.opengl.GLES31

internal object GLState {
    fun restoreDefaults() {
        disableDepthTest()
        disableBlend()
        enableDepthWrites()
        enableColorWrites()
    }

    fun enableDepthTest() {
        GLES31.glEnable(GLES31.GL_DEPTH_TEST) // 开启更新深度缓冲区，视觉效果更真实
    }
    fun disableDepthTest() {
        GLES31.glDisable(GLES31.GL_DEPTH_TEST)
    }

    fun enableBlend() {
        GLES31.glEnable(GLES31.GL_BLEND)
    }

    fun enableBlend(sfactor: Int, dfactor: Int) {
        GLES31.glEnable(GLES31.GL_BLEND)
        blendFunc(sfactor, dfactor)
    }

    fun blendFunc(sfactor: Int, dfactor: Int) {
        GLES31.glBlendFunc(sfactor, dfactor)
    }

    fun disableBlend() {
        GLES31.glDisable(GLES31.GL_BLEND)
    }

    fun enableDepthWrites() {
        GLES31.glDepthMask(true)
    }

    fun disableDepthWrites() {
        GLES31.glDepthMask(false)
    }

    fun enableColorWrites() {
        GLES31.glColorMask(true, true, true, true)
    }

    fun disableColorWrites() {
        GLES31.glColorMask(false, false, false, false)
    }
}
