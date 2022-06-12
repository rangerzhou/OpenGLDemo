package com.aptiv.opengldemo.renderer

import android.content.Context
import com.aptiv.opengldemo.renderer.core.ObjFile

/**
 *
 */
internal class MeshCache(private val context: Context) {

    private val cache = hashMapOf<Int, ObjFile>()

    fun get(resource: Int): ObjFile {

        // Try getting the mesh from the memory cache
        val cachedObj = cache[resource]
        if (cachedObj != null) {
            return cachedObj
        }

        val obj = ObjFile(resource, context.resources)
        cache[resource] = obj
        return obj
    }
}
