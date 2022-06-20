package com.aptiv.adas.core.model

import com.aptiv.adas.core.model.common.Curve
import com.aptiv.adas.core.model.common.Side

data class RoadEdge(
    val isAvailable: Boolean,
    val curve: Curve,
    val side: Side,
    val type: Type
) {

    fun isValid(): Boolean {
        return side != Side.Invalid && type != Type.Invalid
    }

    enum class Type(val value: Byte) {
        Undecided(0),
        RoadEdge(1),
        ElevatedStructure(2),
        Curb(3),
        ConesPoles(4),
        ParkedCars(5),
        Invalid(Byte.MAX_VALUE);

        companion object {
            private val reverseValues: Map<Byte, Type> = values().associateBy { it.value }
            fun valueFrom(i: Byte) = reverseValues[i] ?: Invalid
        }
    }
}
