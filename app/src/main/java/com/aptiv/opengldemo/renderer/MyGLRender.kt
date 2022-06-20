package com.aptiv.opengldemo.renderer

import android.content.res.Resources
import android.graphics.SurfaceTexture
import android.opengl.GLES31.*
import android.opengl.GLSurfaceView
import android.provider.SyncStateContract.Helpers.update
import com.aptiv.adas.core.model.*
import com.aptiv.adas.core.model.common.Curve
import com.aptiv.adas.core.model.common.Side
import com.aptiv.opengldemo.model.AdasState
import com.aptiv.opengldemo.renderer.core.MatrixState
import com.aptiv.opengldemo.renderer.objects.HostCar
import com.aptiv.opengldemo.renderer.objects.Triangle
import glm_.Java
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
    private var adasState: AdasState? = null
    private var distTraveled = 0.0f
    private var distTraveledDelta = 0.0f
    private var timePrev = 0L

    init {
        //0.005453125f
        val idx =0
        val hostInfo = HostInfo(1.0f, 0f, 6.0f,  HostInfo.Gear.Park, HostInfo.TurnIndication.Neutral, false)
        val detectedObject=DetectedObject(id=1, 8.838493f, 9.465311f, 6.4040144E-4f, -2.2112088f, 0.5f, 0.5f, 1.752738f,  DetectedObject.Type.Pedestrian)
        val detectedObjects: List<DetectedObject> = listOf(detectedObject)
        val laneMarkers: List<LaneMarker> = emptyList()
        val roadEdgeFirst = RoadEdge(isAvailable=false, curve= Curve(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), Side.Left, RoadEdge.Type.RoadEdge)
        val roadEdgeSecond = RoadEdge(isAvailable=false, curve= Curve(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), Side.Right, RoadEdge.Type.RoadEdge)
        val roadEdges: Pair<RoadEdge, RoadEdge> = Pair(roadEdgeFirst,roadEdgeSecond)
        val predictedPath =PredictedPath(curve=Curve(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), 2.2208f, false)
        adasState = AdasState(idx,hostInfo,detectedObjects,laneMarkers,roadEdges,predictedPath)
    }

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
        //MatrixState.setProjectionFrustum(-ratio, ratio, -1f, 1f, 3f, 7f) // 根据参数创建一个透视投影矩阵
        //MatrixState.setCamera(0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f) // 创建相机视图矩阵
        viewToScreen = Java.glm.perspective(Java.glm.radians(33.0f), ratio, 1.0f, 2000.0f) // 透视投影矩阵
    }

    override fun onDrawFrame(unused: GL10?) {
        //Timber.i("onDrawFrame adasState: $adasState")
        val frameState = adasState?:return
        glClear(GL_COLOR_BUFFER_BIT) // 根据清除颜色设置窗口颜色

        update(frameState)

        val worldToView = getWorldToView() // 相机视图矩阵
        val worldToScreen = viewToScreen * worldToView // 总变换矩阵
        //triangle.draw()
        hostCar.draw(/*frameState,*/worldToScreen, worldToView/*, time*/)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        Timber.i("onFrameAvailable")
        myGLSurfaceView.requestRender() // 配合 MyGLSurfaceView 中的 renderMode 使用
    }

    private fun getWorldToView(): Mat4 {
        return camera.getWorldToView()
    }

    private fun update(frameState: AdasState){
        val time = System.currentTimeMillis()
        val timeDelta: Float

        if (timePrev != 0L) {
            timeDelta = (time - timePrev) * 0.001f // 两次 draw 的时间增量：秒

            val signedSpeed = if (frameState.hostInfo.gear == HostInfo.Gear.Reverse) { // 有符号速度
                -frameState.hostInfo.speed // 倒挡
            } else {
                frameState.hostInfo.speed
            }

            distTraveledDelta = signedSpeed * timeDelta // 速度 * 时间增量 = 距离

            // Modulo operation needed due to lanes rendering incorrectly on the ICC2
            // when distTraveled exceeds a big enough value and starts rounding off.
            // 54.0f Just get from testing.
            distTraveled = (distTraveled + distTraveledDelta) % (54.0f)
        }
        camera.update(
            frameState.hostInfo.speedKmH,
            -frameState.hostInfo.yawRate,
            frameState.hostInfo.gear
        )
    }

}
