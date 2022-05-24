package com.aptiv.OpenGLDemo.renderer.core

import android.opengl.GLES31
import android.opengl.GLU
import java.nio.ByteBuffer
import java.nio.ByteOrder
import timber.log.Timber

internal object GLHelper {

    private const val SIZE_OF_SHORT = 2
    private const val SIZE_OF_INT = 4
    private const val SIZE_OF_FLOAT = 4

    const val COORDS_PER_VEC2 = 2
    const val COORDS_PER_VEC3 = 3
    const val VERTICES_PER_FACE = 3

    fun deleteBuffer(buffer: Int) {
        GLES31.glDeleteBuffers(1, intArrayOf(buffer), 0)
    }

    fun createBuffer(type: Int, content: ByteBuffer, usage: Int): Int {
        // Generate buffer
        val posBuffer = IntArray(1)
        GLES31.glGenBuffers(1, posBuffer, 0)
        check(posBuffer[0] != 0)

        // Fill with content
        GLES31.glBindBuffer(type, posBuffer[0])
        GLES31.glBufferData(type, content.capacity(), content, usage)
        GLES31.glBindBuffer(type, 0)

        return posBuffer[0]
    }

    fun createByteBuffer(floats: FloatArray): ByteBuffer {
        val bb = ByteBuffer.allocateDirect(floats.size * SIZE_OF_FLOAT)
        bb.order(ByteOrder.nativeOrder())

        val fb = bb.asFloatBuffer()
        fb.put(floats)
        fb.position(0)

        return bb
    }

    fun createByteBuffer(ints: IntArray): ByteBuffer {
        val bb = ByteBuffer.allocateDirect(ints.size * SIZE_OF_INT)
        bb.order(ByteOrder.nativeOrder())

        val ib = bb.asIntBuffer()
        ib.put(ints)
        ib.position(0)

        return bb
    }

    fun createByteBuffer(shorts: ShortArray): ByteBuffer {
        val bb = ByteBuffer.allocateDirect(shorts.size * SIZE_OF_SHORT)
        bb.order(ByteOrder.nativeOrder())

        val sb = bb.asShortBuffer()
        sb.put(shorts)
        sb.position(0)

        return bb
    }

    fun checkForError() {
        var errorCount = 0
        var error = GLES31.glGetError()
        while (error != GLES31.GL_NO_ERROR) {
            Timber.e("OpenGL Error: ${GLU.gluErrorString(error)}")
            error = GLES31.glGetError()
            errorCount ++
        }

        check(errorCount == 0)
    }
}
