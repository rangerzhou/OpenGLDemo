package com.aptiv.opengldemo.renderer.meshes

import android.opengl.GLES31
import com.aptiv.opengldemo.renderer.core.GLHelper.COORDS_PER_VEC2
import com.aptiv.opengldemo.renderer.core.GLHelper.COORDS_PER_VEC3
import com.aptiv.opengldemo.renderer.core.GLHelper.createBuffer
import com.aptiv.opengldemo.renderer.core.GLHelper.createByteBuffer
import com.aptiv.opengldemo.renderer.core.GLHelper.deleteBuffer
import com.aptiv.opengldemo.renderer.core.ObjFile
import com.aptiv.opengldemo.renderer.meshes.Mesh.Companion.NOT_USED
import glm_.vec3.Vec3

internal class MeshObj(obj: ObjFile) : Mesh {

    private var positionBuffer: Int
    private var normalBuffer: Int
    private var uvBuffer: Int
    private var indexBuffer: Int

    private val indexCount: Int

    private var drawInProgress = false

    private var attribPos = NOT_USED
    private var attribNormal = NOT_USED
    private var attribUV = NOT_USED

    private val bbMin: Vec3
    private val bbMax: Vec3

    init {
        val positions = createByteBuffer(obj.positions)
        positionBuffer = createBuffer(GLES31.GL_ARRAY_BUFFER, positions, GLES31.GL_STATIC_DRAW)

        val normals = createByteBuffer(obj.normals)
        normalBuffer = createBuffer(GLES31.GL_ARRAY_BUFFER, normals, GLES31.GL_STATIC_DRAW)

        val uvs = createByteBuffer(obj.uvs)
        uvBuffer = createBuffer(GLES31.GL_ARRAY_BUFFER, uvs, GLES31.GL_STATIC_DRAW)

        val indices = createByteBuffer(obj.indices)
        indexBuffer = createBuffer(GLES31.GL_ELEMENT_ARRAY_BUFFER, indices, GLES31.GL_STATIC_DRAW)

        indexCount = obj.indices.size

        // Calculate bounding box
        check(obj.positions.size % 3 == 0) { "Unknown positions count" }
        val vertexCount = obj.positions.size / 3

        var bbMinX = Float.MAX_VALUE
        var bbMinY = Float.MAX_VALUE
        var bbMinZ = Float.MAX_VALUE

        var bbMaxX = -Float.MAX_VALUE
        var bbMaxY = -Float.MAX_VALUE
        var bbMaxZ = -Float.MAX_VALUE

        for (n in 0 until vertexCount) {
            val x = obj.positions[n * 3 + 0]
            val y = obj.positions[n * 3 + 1]
            val z = obj.positions[n * 3 + 2]

            bbMinX = if (x < bbMinX) { x } else { bbMinX }
            bbMinY = if (y < bbMinY) { y } else { bbMinY }
            bbMinZ = if (z < bbMinZ) { z } else { bbMinZ }

            bbMaxX = if (x > bbMaxX) { x } else { bbMaxX }
            bbMaxY = if (y > bbMaxY) { y } else { bbMaxY }
            bbMaxZ = if (z > bbMaxZ) { z } else { bbMaxZ }
        }

        bbMin = Vec3(bbMinX, bbMinY, bbMinZ)
        bbMax = Vec3(bbMaxX, bbMaxY, bbMaxZ)
    }

    override fun drawBegin(aPosition: Int, aNormal: Int, aUV: Int) {
        //check(!drawInProgress)

        GLES31.glEnableVertexAttribArray(aPosition)
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, positionBuffer)
        GLES31.glVertexAttribPointer(aPosition, COORDS_PER_VEC3, GLES31.GL_FLOAT, false, 0, 0)

        if (aNormal != NOT_USED) {
            GLES31.glEnableVertexAttribArray(aNormal)
            GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, normalBuffer)
            GLES31.glVertexAttribPointer(aNormal, COORDS_PER_VEC3, GLES31.GL_FLOAT, false, 0, 0)
        }

        if (aUV != NOT_USED) {
            GLES31.glEnableVertexAttribArray(aUV)
            GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, uvBuffer)
            GLES31.glVertexAttribPointer(aUV, COORDS_PER_VEC2, GLES31.GL_FLOAT, false, 0, 0)
        }

        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0)

        GLES31.glBindBuffer(GLES31.GL_ELEMENT_ARRAY_BUFFER, indexBuffer)

        attribPos = aPosition
        attribNormal = aNormal
        attribUV = aUV

        drawInProgress = true
    }

    override fun drawEnd() {
        check(drawInProgress)

        GLES31.glBindBuffer(GLES31.GL_ELEMENT_ARRAY_BUFFER, 0)

        GLES31.glDisableVertexAttribArray(attribPos)

        if (attribNormal != NOT_USED) {
            GLES31.glDisableVertexAttribArray(attribNormal)
        }

        if (attribUV != NOT_USED) {
            GLES31.glDisableVertexAttribArray(attribUV)
        }

        drawInProgress = false
    }

    override fun getBBMin(): Vec3 {
        return bbMin
    }

    override fun getBBMax(): Vec3 {
        return bbMax
    }

    override fun draw() {
        check(drawInProgress)
        GLES31.glDrawElements(GLES31.GL_TRIANGLES, indexCount, GLES31.GL_UNSIGNED_INT, 0)
    }

    fun drawOnce(aPosition: Int, aNormal: Int = NOT_USED, aUV: Int = NOT_USED) {
        drawBegin(aPosition, aNormal, aUV)
        draw()
        drawEnd()
    }

    override fun destroy() {
        deleteBuffer(positionBuffer)
        positionBuffer = 0

        deleteBuffer(normalBuffer)
        normalBuffer = 0

        deleteBuffer(uvBuffer)
        uvBuffer = 0

        deleteBuffer(indexBuffer)
        indexBuffer = 0
    }
}
