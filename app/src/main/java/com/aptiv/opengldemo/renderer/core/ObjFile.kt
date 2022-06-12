package com.aptiv.opengldemo.renderer.core

import android.content.res.Resources
import com.aptiv.opengldemo.renderer.core.GLHelper.COORDS_PER_VEC2
import com.aptiv.opengldemo.renderer.core.GLHelper.COORDS_PER_VEC3
import com.aptiv.opengldemo.renderer.core.GLHelper.VERTICES_PER_FACE
import de.javagl.obj.ObjReader
import java.io.Serializable

internal class ObjFile(meshRes: Int, resources: Resources) : Serializable {

    val positions: FloatArray
    val normals: FloatArray
    val uvs: FloatArray
    val indices: IntArray

    data class Vertex(
        val x: Float,
        val y: Float,
        val z: Float,
        val nX: Float,
        val nY: Float,
        val nZ: Float,
        val u: Float,
        val v: Float
    )

    init {
        // Read obj file
        val objStream = resources.openRawResource(meshRes)
        val obj = ObjReader.read(objStream)

        // Generate list of unique vertices and populate the indices array
        indices = IntArray(obj.numFaces * VERTICES_PER_FACE)

        val uniqueVertices = hashMapOf<Vertex, Int>()

        val normal = FloatArray(3) // Reused storage object
        val uv = FloatArray(2) // Reused storage object

        for (n in 0 until obj.numFaces) {
            val face = obj.getFace(n)
            check(VERTICES_PER_FACE == face.numVertices)

            for (m in 0 until VERTICES_PER_FACE) {
                if (face.containsNormalIndices()) {
                    val normalTmp = obj.getNormal(face.getNormalIndex(m))
                    normal[0] = normalTmp[0]
                    normal[1] = normalTmp[1]
                    normal[2] = normalTmp[2]
                } else {
                    normal[0] = 0.0f
                    normal[1] = 1.0f
                    normal[2] = 0.0f
                }

                if (face.containsTexCoordIndices()) {
                    val uvTmp = obj.getTexCoord(face.getTexCoordIndex(m))
                    uv[0] = uvTmp[0]
                    uv[1] = uvTmp[1]
                } else {
                    uv[0] = 0.0f
                    uv[1] = 0.0f
                }

                val pos = obj.getVertex(face.getVertexIndex(m))
                val vertex = Vertex(
                    pos[0], pos[1], pos[2],
                    normal[0], normal[1], normal[2],
                    uv[0], uv[1]
                )

                // Test if the same vertex already exists. Use the previous vertex if it has already
                // been added or add the current vertex otherwise.
                var index = uniqueVertices[vertex]
                if (index == null) {
                    index = uniqueVertices.size
                    uniqueVertices[vertex] = index
                }

                // Keep track of which vertices this face is constructed of
                indices[n * VERTICES_PER_FACE + m] = index
            }
        }

        // Pack the unique vertices into separate buffers for positions, normals and uvs
        positions = FloatArray(uniqueVertices.size * COORDS_PER_VEC3)
        normals = FloatArray(uniqueVertices.size * COORDS_PER_VEC3)
        uvs = FloatArray(uniqueVertices.size * COORDS_PER_VEC2)

        for ((vertex, index) in uniqueVertices) {
            positions[index * COORDS_PER_VEC3 + 0] = vertex.x
            positions[index * COORDS_PER_VEC3 + 1] = vertex.y
            positions[index * COORDS_PER_VEC3 + 2] = vertex.z

            normals[index * COORDS_PER_VEC3 + 0] = vertex.nX
            normals[index * COORDS_PER_VEC3 + 1] = vertex.nY
            normals[index * COORDS_PER_VEC3 + 2] = vertex.nZ

            uvs[index * COORDS_PER_VEC2 + 0] = vertex.u
            uvs[index * COORDS_PER_VEC2 + 1] = vertex.v
        }
    }
}
