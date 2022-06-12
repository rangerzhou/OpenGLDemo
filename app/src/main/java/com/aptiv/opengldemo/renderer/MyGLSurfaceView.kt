package com.aptiv.opengldemo.renderer

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class MyGLSurfaceView(context: Context, attr: AttributeSet) : GLSurfaceView(context, attr) {
    private val renderer = MyGLRender(context.resources, MeshCache(context), this)

    init {
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(2) // 设置 EGL 版本，模拟器运行必须设置 2
        setRenderer(renderer) // 设置自定义渲染器
        //renderMode = RENDERMODE_WHEN_DIRTY // 设置渲染模式，按需渲染，效率高
    }
}