package com.aptiv.opengldemo.renderer.core

import android.content.res.Resources
import android.opengl.GLES31
import timber.log.Timber

open class Shader(vertRes: Int, fragRes: Int, resources: Resources) {

    private var program: Int

    companion object {
        private fun getShaderCode(resources: Resources, rawResId: Int): String {
            val stream = resources.openRawResource(rawResId)

            val buffer = ByteArray(stream.available())
            stream.read(buffer)

            stream.close()

            return String(buffer)
        }

        private fun loadShader(type: Int, shaderCode: String): Int {
            val shader = GLES31.glCreateShader(type) // 创建着色器

            GLES31.glShaderSource(shader, shaderCode) // 将着色器代码添加到着色器
            GLES31.glCompileShader(shader) // 编译着色器

            // 检验是否编译成功
            val compiled = IntArray(1)
            GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compiled, 0)

            if (compiled[0] == 0) { // 等于 0 说明编译失败
                Timber.e("Could not compile shader $type: ${GLES31.glGetShaderInfoLog(shader)}")
                throw IllegalStateException()
            }

            return shader
        }
    }

    init {
        // 从 glsl 文件获取着色器代码
        val vertexShaderCode = getShaderCode(resources, vertRes)
        val fragmentShaderCode = getShaderCode(resources, fragRes)

        // 编译着色器
        val vertexShader = loadShader(GLES31.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES31.glCreateProgram()

        GLES31.glAttachShader(program, vertexShader)
        GLES31.glAttachShader(program, fragmentShader)
        GLES31.glLinkProgram(program)

        val linkStatus = IntArray(1)
        GLES31.glGetProgramiv(program, GLES31.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES31.GL_TRUE) {
            Timber.e("Could not link program: ${GLES31.glGetProgramInfoLog(program)}")
            throw IllegalStateException()
        }

        GLES31.glDeleteShader(vertexShader)
        GLES31.glDeleteShader(fragmentShader)
    }

    fun use() {
        GLES31.glUseProgram(program)
    }

    fun getUniformLocation(name: String): Int {
        return GLES31.glGetUniformLocation(program, name)
    }

    fun getAttribLocation(name: String): Int {
        return GLES31.glGetAttribLocation(program, name)
    }

    fun destroy() {
        GLES31.glDeleteProgram(program)
        program = 0
    }

    fun enableVertexAttribArray(positionHandle: Int) {
        GLES31.glEnableVertexAttribArray(positionHandle)
    }
}
