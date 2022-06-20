package com.aptiv.opengldemo.model

import com.aptiv.adas.core.model.*
import kotlin.math.abs

data class AdasState(
    val idx: Int,
    val hostInfo: HostInfo,
    val detectedObjects: List<DetectedObject>,
    val laneMarkers: List<LaneMarker>,
    val roadEdges: Pair<RoadEdge, RoadEdge>,
    val predictedPath: PredictedPath
) {
    companion object {
        private var lastIdx:Int = 0
    }

    fun isValid(): Boolean {
        val isIdxValid = abs(idx - lastIdx) < 20
        lastIdx = idx
        return isIdxValid && hostInfo.isValid()
    }
}
