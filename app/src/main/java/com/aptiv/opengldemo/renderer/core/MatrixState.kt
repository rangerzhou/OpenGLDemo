package com.aptiv.opengldemo.renderer.core

import android.opengl.Matrix

class MatrixState {
    companion object {
        private val currentMatrix = FloatArray(16) // 当前变换矩阵，基本变换矩阵
        private val viewMatrix = FloatArray(16) // 摄像机观察矩阵
        private val projectionMatrix = FloatArray(16) // 投影矩阵
        private val mvpMatrix = FloatArray(16) // 总变换矩阵

        private val stack = Array(10) { FloatArray(16) } // 用于保存变换矩阵的栈
        private var stackTop = -1 // 栈顶索引

        /**
         * 初始化矩阵用于旋转
         */
        fun setInitStack() {
            Matrix.setRotateM(currentMatrix, 0, 0f, 1f, 0f, 0f)
        }

        fun pushMatrix() {
            stackTop++
            for (item in 0 until 16) {
                stack[stackTop][item] = currentMatrix[item]
            }
        }

        fun popMatrix() {
            for (item in 0 until 16) {
                currentMatrix[item] = stack[stackTop][item]
            }
            stackTop--
        }

        /**
         * 沿 x、y、z 轴方向进行平移变换的方法
         */
        fun translate(x: Float, y: Float, z: Float) {
            Matrix.translateM(currentMatrix, 0, x, y, z) //
        }

        /**
         * 沿 x、y、z 轴方向进行旋转变换的方法
         */
        fun rotate(angle: Float, x: Float, y: Float, z: Float) {
            Matrix.rotateM(currentMatrix, 0, angle, x, y, z)
        }

        /**
         * 沿 x、y、z 轴方向进行缩放变换的方法
         */
        fun scale(x: Float, y: Float, z: Float) {
            Matrix.scaleM(currentMatrix, 0, x, y, z)
        }

        /**
         * 设置摄像机
         * @param eyeX, eyeY, eyeZ - 摄像机位置的 x、y、z 坐标
         * @param centerX, centerY, centerZ - 观察目标点 x、y、z 坐标
         * @param upX, upY, upZ - 摄像机 up 向量在 x、y、z 轴上的分量
         */
        fun setCamera(
            eyeX: Float, eyeY: Float, eyeZ: Float,
            centerX: Float, centerY: Float, centerZ: Float,
            upX: Float, upY: Float, upZ: Float
        ) {
            Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
        }

        /**
         * 设置正交投影
         * @param left,right - 近平面 left、right 边的 x 坐标
         * @param bottom,top - 近平面 bottom、top 边的 y 坐标
         * @param near,far - 近平面、远平面距离摄像机（视点）的距离
         */
        fun setProjectionOrtho(
            left: Float, right: Float,
            bottom: Float, top: Float,
            near: Float, far: Float
        ) {
            Matrix.orthoM(projectionMatrix, 0, left, right, bottom, top, near, far)
        }

        /**
         * 设置透视投影
         * 参数同 setProjectionOrtho
         */
        fun setProjectionFrustum(
            left: Float, right: Float,
            bottom: Float, top: Float,
            near: Float, far: Float
        ) {
            Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far)
        }

        /**
         * 计算产生总变换矩阵
         */
        fun getFinalMatrix(): FloatArray {
            // 摄像机矩阵乘以基本变换矩阵
            Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, currentMatrix, 0)
            // 投影矩阵乘以上一步的结果矩阵
            Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0)
            return mvpMatrix
        }


    }

}