package com.aptiv.opengldemo.renderer

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

        private val TARGET_SLOW = Vec3(0.0f, 0.0f, 3.0f)
        private val TARGET_FAST = Vec3(0.0f, 0.0f, -6.7f)
        private val TARGET_TOP_VIEW_SLOW = Vec3(0.0f, 0.0f, 4.2f)
        private val TARGET_TOP_VIEW_FAST = Vec3(0.0f, 0.0f, -8.0f)

        private const val ANGLE_TILT_FAST = 0.2f
        private const val ANGLE_TILT_SLOW = PI.toFloat() / 2.0f - 0.1f
        private const val ANGLE_TILT_TOP_VIEW = PI.toFloat() / 2.0f - 0.01f

        private const val DISTANCE_FAST = 20.0f
        private const val DISTANCE_SLOW = 30.0f
        private const val DISTANCE_TOP_VIEW_SLOW = 30.0f
        private const val DISTANCE_TOP_VIEW_FAST = 60.0f

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

    /*fun update(speed: Float, yawRate: Float, gear: HostInfo.Gear) {
        // Get interpolation position [0.0, 1.0] based on the speed
        val signedSpeed = if (gear == HostInfo.Gear.Reverse) { -speed } else { speed }
        val speedWithinRange = clamp(signedSpeed, SPEED_MIN, SPEED_MAX)
        val interpolation = (speedWithinRange - SPEED_MIN) / (SPEED_MAX - SPEED_MIN)

        // Find new camera parameters based on if in top view or not
        val targetNew: Vec3
        val angleTiltNew: Float
        val angleTurnNew: Float
        val distanceNew: Float

        if (isTopView) {
            targetNew = glm.mix(TARGET_TOP_VIEW_SLOW, TARGET_TOP_VIEW_FAST, interpolation)
            angleTiltNew = ANGLE_TILT_TOP_VIEW
            angleTurnNew = 0.0f
            distanceNew = glm.mix(DISTANCE_TOP_VIEW_SLOW, DISTANCE_TOP_VIEW_FAST, interpolation)
        } else {
            targetNew = glm.mix(TARGET_SLOW, TARGET_FAST, interpolation)
            angleTiltNew = glm.mix(ANGLE_TILT_SLOW, ANGLE_TILT_FAST, interpolation)

            // Scale the turning angle by the interpolation position, which is based on the speed,
            // to limit the turning while slowing down. The view will otherwise turn a lot while
            // maneuvering the car at slow speed since the yaw rate is typically high then.
            val turnStrength = interpolation * TURN_STRENGTH
            angleTurnNew = clamp(yawRate * turnStrength, -ANGLE_TURN_MAX, ANGLE_TURN_MAX)

            distanceNew = glm.mix(DISTANCE_SLOW, DISTANCE_FAST, interpolation)
        }

        // Interpolate towards the new values to get smooth transitions between top view and normal
        // view while also filtering out noise
        target = glm.mix(targetNew, target, SMOOTHNESS)
        angleTilt = glm.mix(angleTiltNew, angleTilt, SMOOTHNESS)
        angleTurn = glm.mix(angleTurnNew, angleTurn, SMOOTHNESS)
        distance = glm.mix(distanceNew, distance, SMOOTHNESS)

        // Update the camera position
        rotation.identity()
        rotation.rotateAssign(angleTilt, LEFT)
        rotation.rotateAssign(angleTurn, UP)

        val dirForward = Vec3(0.0f, 0.0f, -distance)
        val dir = Vec3(rotation * Vec4(dirForward, 0.0f))

        pos = target - dir
    }*/

    fun toggleCamera() {
        isTopView = !isTopView
    }

    fun getWorldToView(): Mat4 {
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
