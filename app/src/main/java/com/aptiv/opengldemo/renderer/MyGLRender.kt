package com.aptiv.opengldemo.renderer

import android.content.res.Resources
import android.graphics.SurfaceTexture
import android.opengl.GLES31.*
import android.opengl.GLSurfaceView
import com.aptiv.opengldemo.renderer.core.MatrixState
import com.aptiv.opengldemo.renderer.objects.HostCar
import com.aptiv.opengldemo.renderer.objects.Triangle
import glm_.mat4x4.Mat4
import timber.log.Timber
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

internal class MyGLRender(private val resources: Resources, private val meshCache: MeshCache, private val myGLSurfaceView: MyGLSurfaceView) :
    GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    //private lateinit var textureID: IntArray
    //private lateinit var surfaceTexture: SurfaceTexture

    private lateinit var triangle: Triangle
    private lateinit var hostCar: HostCar
    private var viewToScreen = Mat4() // 投影矩阵
    private val camera = Camera()

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        Timber.i("onSurfaceCreated")
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f) // 设置清除颜色: 黑色
        /*textureID = IntArray(1)
        surfaceTexture = SurfaceTexture(textureID[0])
        surfaceTexture.setOnFrameAvailableListener(this)
        glGenTextures(textureID.size, textureID, 0)*/

        // 初始化形状
        triangle = Triangle(resources, myGLSurfaceView)
        hostCar = HostCar(resources, meshCache)

    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        Timber.i("onSurfaceChanged")
        // 设置画布的位置和宽高（设置视口，视口是显示屏上指定的矩形区域，x 和 y 是视口的左下角坐标值[x 轴向右，y 轴向上]，后两个参数是矩形的宽高）
        glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()
        MatrixState.setProjectionFrustum(-ratio, ratio, -1f, 1f, 3f, 7f) // 根据参数创建一个透视投影矩阵
        MatrixState.setCamera(0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f) // 创建相机视图矩阵
        //viewToScreen = Java.glm.perspective(Java.glm.radians(33.0f), ratio, 1.0f, 2000.0f)
    }

    override fun onDrawFrame(unused: GL10?) {
        Timber.i("onDrawFrame")
        glClear(GL_COLOR_BUFFER_BIT) // 根据清除颜色设置窗口颜色

        val worldToView = getWorldToView() // 相机视图矩阵
        val worldToScreen = viewToScreen * worldToView // 总变换矩阵
        //triangle.draw()
        hostCar.draw(/*frameState*/ worldToScreen, worldToView, /*time*/)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        Timber.i("onFrameAvailable")
        myGLSurfaceView.requestRender() // 配合 MyGLSurfaceView 中的 renderMode 使用
    }

    private fun getWorldToView(): Mat4 {
        return camera.getWorldToView()
    }

}
