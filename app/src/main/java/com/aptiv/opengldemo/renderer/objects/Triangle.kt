package com.aptiv.opengldemo.renderer.objects

import android.content.res.Resources
import android.opengl.GLES31
import com.aptiv.opengldemo.renderer.core.GLHelper.COORDS_PER_VEC3
import com.aptiv.opengldemo.renderer.core.GLHelper.createByteBuffer
import com.aptiv.opengldemo.renderer.core.MatrixState
import com.aptiv.opengldemo.renderer.shaders.ShaderTriangle
import java.nio.ByteBuffer

internal class Triangle(resources: Resources) {
    private val shader: ShaderTriangle
    private var vertexBuffer: ByteBuffer // 顶点坐标数据缓冲
    private var colorBuffer: ByteBuffer // 顶点颜色数据缓冲
    var xAngle = 0f


    companion object {
        const val COORDS_PER_VERTEX = 3
        const val COORDS_PER_COLOR = 4
        const val SIZE_OF_FLOAT = 4

        // 1. 准备数据
        var triangleCoords = floatArrayOf(     // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f,      // top
            -0.5f, -0.311004243f, 0.0f,    // bottom left
            0.5f, -0.311004243f, 0.0f      // bottom right
        )
        val vertexCount: Int = triangleCoords.size / COORDS_PER_VEC3
        //const val vertexStride: Int = COORDS_PER_VERTEX * SIZE_OF_FLOAT // 4 bytes per vertex

        val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

        var colors = floatArrayOf(
            1f, 0f, 0f, 0f, // 红色
            0f, 1f, 0f, 0f, // 绿色
            0f, 0f, 1f, 0f // 蓝色
        )

    }

    init {
        shader = ShaderTriangle(resources)
        vertexBuffer = createByteBuffer(triangleCoords) // 2. 将顶点数据经过基本处理后送入 VBO
        colorBuffer = createByteBuffer(colors)
    }

    fun draw(/*mvPMatrix: FloatArray*/) {
        shader.use()

        // 3. 通过 VBO 将顶点数据传进渲染管线，管线进行基本处理后再传递给顶点着色器
        GLES31.glVertexAttribPointer(
            shader.positionHandle, // 顶点属性索引值
            COORDS_PER_VERTEX, // 每个顶点属性的数据数量，值为 1-4
            GLES31.GL_FLOAT, // 顶点属性数据的数据类型
            false, // 非浮点类型数据转化为浮点类型数据时，是否需要规格化
            COORDS_PER_VERTEX * SIZE_OF_FLOAT, // 相邻顶点数据的间隔，单位为字节
            vertexBuffer // 顶点属性数据缓冲
        )
        GLES31.glVertexAttribPointer(
            shader.mColorHandle,
            COORDS_PER_COLOR,
            GLES31.GL_FLOAT,
            false,
            COORDS_PER_COLOR * SIZE_OF_FLOAT,
            colorBuffer
        )
        GLES31.glEnableVertexAttribArray(shader.positionHandle) // 启用索引为 positionHandle 的顶点数组
        GLES31.glEnableVertexAttribArray(shader.mColorHandle) // 启用索引为 mColorHandle 的顶点数组
        //GLES31.glUniform4fv(shader.mColorHandle, 1, color, 0)
        //GLES31.glUniformMatrix4fv(shader.vPMatrixHandle, 1, false, mvPMatrix, 0)
        MatrixState.setInitStack() // 初始化变换矩阵
        MatrixState.translate(0f, 0f, 1f) // 设置沿 Z 轴正向平移
        MatrixState.rotate(xAngle, 1f, 1f, 1f) // 设置绕 x y z 轴旋转
        GLES31.glUniformMatrix4fv(
            shader.vPMatrixHandle,
            1,
            false,
            /*getFinalMatrix(mMMatrix)*/MatrixState.getFinalMatrix(), // 获取总变换矩阵
            0
        )

        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount) // 画！
        GLES31.glDisableVertexAttribArray(shader.positionHandle)
    }
}
