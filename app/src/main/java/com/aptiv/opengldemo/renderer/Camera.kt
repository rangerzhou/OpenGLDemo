package com.aptiv.opengldemo.renderer

import androidx.core.math.MathUtils.clamp
import com.aptiv.adas.core.model.HostInfo
import glm_.glm
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import kotlin.math.PI

internal class Camera {

    companion object {
        private val UP = Vec3(0.0f, 1.0f, 0.0f)
        private val LEFT = Vec3(-1.0f, 0.0f, 0.0f)

        private const val SMOOTHNESS = 0.98f

        private const val SPEED_MIN = -5.0f
        private const val SPEED_MAX = 15.0f
        // 默认视图和 top_view 视图下的观测目标点（car）向量
        private val TARGET_SLOW = Vec3(0.0f, 0.0f, 3.0f)
        private val TARGET_FAST = Vec3(0.0f, 0.0f, -6.7f)
        private val TARGET_TOP_VIEW_SLOW = Vec3(0.0f, 0.0f, 4.2f)
        private val TARGET_TOP_VIEW_FAST = Vec3(0.0f, 0.0f, -8.0f)
        // 倾斜角度
        private const val ANGLE_TILT_FAST = 0.2f
        private const val ANGLE_TILT_SLOW = PI.toFloat() / 2.0f - 0.1f
        private const val ANGLE_TILT_TOP_VIEW = PI.toFloat() / 2.0f - 0.01f
        // 距离
        private const val DISTANCE_FAST = 20.0f
        private const val DISTANCE_SLOW = 30.0f
        private const val DISTANCE_TOP_VIEW_SLOW = 30.0f
        private const val DISTANCE_TOP_VIEW_FAST = 60.0f
        // 转弯角度
        private const val ANGLE_TURN_MAX = PI.toFloat() / 6.0f
        private const val TURN_STRENGTH = 1.7f
    }

    private var isTopView = false

    private var pos = Vec3(0.0f)
    private var target = TARGET_FAST

    private var rotation = Mat4()

    private var distance = DISTANCE_FAST

    private var angleTilt = ANGLE_TILT_FAST
    private var angleTurn = 0.0f
    // 根据速度计算插值
    fun update(speed: Float, yawRate: Float, gear: HostInfo.Gear) {
        // Get interpolation position [0.0, 1.0] based on the speed
        val signedSpeed = if (gear == HostInfo.Gear.Reverse) { -speed } else { speed } // 有符号速度值
        // 获取当前速度、最小速度、最大速度之间的中间值，此处的最小最大速度都是针对的低速情况，为了后续处理低速时的插值计算
        val speedWithinRange = clamp(signedSpeed, SPEED_MIN, SPEED_MAX)
        val interpolation = (speedWithinRange - SPEED_MIN) / (SPEED_MAX - SPEED_MIN) // 根据速度中间值获取插值

        // Find new camera parameters based on if in top view or not
        val targetNew: Vec3
        val angleTiltNew: Float
        val angleTurnNew: Float
        val distanceNew: Float

        if (isTopView) { // 不同视图下的不同操作，isTopView == true 时视线垂直于路面
            // 线性插值函数，return x(1-a) +y*a，根据上面计算出的插值返回线性混合的值，插值越大，越倾向于 FAST
            targetNew = glm.mix(TARGET_TOP_VIEW_SLOW, TARGET_TOP_VIEW_FAST, interpolation) // 目标位置向量
            angleTiltNew = ANGLE_TILT_TOP_VIEW // 倾斜角度
            angleTurnNew = 0.0f // 转弯角度
            distanceNew = glm.mix(DISTANCE_TOP_VIEW_SLOW, DISTANCE_TOP_VIEW_FAST, interpolation) // 距离
        } else { // false 时是斜视角度
            targetNew = glm.mix(TARGET_SLOW, TARGET_FAST, interpolation)
            angleTiltNew = glm.mix(ANGLE_TILT_SLOW, ANGLE_TILT_FAST, interpolation)

            // Scale the turning angle by the interpolation position, which is based on the speed,
            // to limit the turning while slowing down. The view will otherwise turn a lot while
            // maneuvering the car at slow speed since the yaw rate is typically high then.
            // 插值的目的是为了避免当速度比较小时视野转向很多的问题
            val turnStrength = interpolation * TURN_STRENGTH
            angleTurnNew = clamp(yawRate * turnStrength, -ANGLE_TURN_MAX, ANGLE_TURN_MAX)

            distanceNew = glm.mix(DISTANCE_SLOW, DISTANCE_FAST, interpolation)
        }

        // Interpolate towards the new values to get smooth transitions between top view and normal
        // view while also filtering out noise
        // 插值以使不同视图切换过渡更平滑
        target = glm.mix(targetNew, target, SMOOTHNESS) // 观测目标位置向量
        angleTilt = glm.mix(angleTiltNew, angleTilt, SMOOTHNESS) // 倾斜角度
        angleTurn = glm.mix(angleTurnNew, angleTurn, SMOOTHNESS) // 转弯角度
        distance = glm.mix(distanceNew, distance, SMOOTHNESS) // 距离

        // Update the camera position 更新相机角度
        rotation.identity()
        rotation.rotateAssign(angleTilt, LEFT) // 摄像机沿 X 轴旋转，视野上下倾斜，比如俯视视图变到斜视视图
        rotation.rotateAssign(angleTurn, UP) // 摄像机沿 Y 轴旋转，视野左右倾斜

        val dirForward = Vec3(0.0f, 0.0f, -distance)
        val dir = Vec3(rotation * Vec4(dirForward, 0.0f))

        pos = target - dir // pos 是摄像机位置向量
    }

    fun toggleCamera() {
        isTopView = !isTopView
    }

    fun getWorldToView(): Mat4 {
        // pos：摄像机位置向量，target：观察目标点位置向量，UP：摄像机 UP 向量
        return glm.lookAt(pos, target, UP) // 返回相机视图矩阵
    }

    fun getFixedWorldToView(): Mat4 {
        val fixedRotation = Mat4()
        fixedRotation.identity()
        fixedRotation.rotateAssign(ANGLE_TILT_FAST, LEFT)
        fixedRotation.rotateAssign(0.0f, UP)
        val fixedDirForward = Vec3(0.0f, 0.0f, -DISTANCE_FAST)
        val fixedDir = Vec3(fixedRotation * Vec4(fixedDirForward, 0.0f))
        val fixedPos = TARGET_FAST - fixedDir
        return glm.lookAt(fixedPos, TARGET_FAST, UP)
    }
}
