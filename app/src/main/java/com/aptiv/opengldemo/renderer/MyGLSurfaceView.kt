package com.aptiv.OpenGLDemo.renderer

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class MyGLSurfaceView(context: Context, attr: AttributeSet) : GLSurfaceView(context, attr) {
    //private val myGLSurfaceView = MyGLSurfaceView(context, attr)
    private val renderer: MyGLRender

    init {
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(2) // 设置 EGL 版本
        renderer = MyGLRender(context.resources, this)

        setRenderer(renderer) // 设置自定义渲染器
        //renderMode = RENDERMODE_WHEN_DIRTY // 设置渲染模式，按需渲染，效率高
    }
}