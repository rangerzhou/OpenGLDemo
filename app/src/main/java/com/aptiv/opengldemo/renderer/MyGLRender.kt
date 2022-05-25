package com.aptiv.opengldemo.renderer

import android.content.res.Resources
import android.graphics.SurfaceTexture
import android.opengl.GLES31.*
import android.opengl.GLSurfaceView
import com.aptiv.opengldemo.renderer.core.MatrixState
import com.aptiv.opengldemo.renderer.objects.Square
import com.aptiv.opengldemo.renderer.objects.Triangle
import timber.log.Timber
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

internal class MyGLRender(private val resources: Resources, private val myGLSurfaceView: MyGLSurfaceView) :
    GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    //private lateinit var textureID: IntArray
    //private lateinit var surfaceTexture: SurfaceTexture

    private lateinit var triangle: Triangle
    private lateinit var square: Square

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        Timber.i("onSurfaceCreated")
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f) // 设置清除颜色: 黑色
        /*textureID = IntArray(1)
        surfaceTexture = SurfaceTexture(textureID[0])
        surfaceTexture.setOnFrameAvailableListener(this)
        glGenTextures(textureID.size, textureID, 0)*/

        // 初始化形状
        triangle = Triangle(resources, myGLSurfaceView)
        //mSquare = Square()

    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        Timber.i("onSurfaceChanged")
        // 设置画布的位置和宽高（设置视口，视口是显示屏上指定的矩形区域，x 和 y 是视口的左下角坐标值[x 轴向右，y 轴向上]，后两个参数是矩形的宽高）
        glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()
        MatrixState.setProjectionFrustum(-ratio, ratio, -1f, 1f, 3f, 7f) // 根据参数创建一个透视投影矩阵
        MatrixState.setCamera(0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f) // 创建相机视图矩阵
    }

    override fun onDrawFrame(unused: GL10?) {
        Timber.i("onDrawFrame")
        glClear(GL_COLOR_BUFFER_BIT) // 根据清除颜色设置窗口颜色

        triangle.draw()
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        Timber.i("onFrameAvailable")
        myGLSurfaceView.requestRender() // 配合 MyGLSurfaceView 中的 renderMode 使用
    }

}
