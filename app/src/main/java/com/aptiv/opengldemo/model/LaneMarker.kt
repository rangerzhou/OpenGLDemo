package com.aptiv.adas.core.model

import com.aptiv.adas.core.model.common.Curve
import com.aptiv.adas.core.model.common.Side

data class LaneMarker(
    val isAvailable: Boolean,
    val curve: Curve,
    val type: Type,
    val color: Color,
    val side: Side,
    val isAdjacent: Boolean
) {

    fun isValid(): Boolean {
        return type != Type.Invalid && color != Color.Invalid && side != Side.Invalid
    }

    enum class Type(val value: Byte) {
        Undecided(0),
        Solid(1), // 固体的
        Dashed(3), //虚线
        DoubleLane(4), // 双车道
        BottsDots(5), // 博斯点
        Invalid(Byte.MAX_VALUE);

        companion object {
            private val reverseValues: Map<Byte, Type> = values().associateBy { it.value }
            fun valueFrom(i: Byte) = reverseValues[i] ?: Invalid
        }
    }

    enum class Color(val value: Byte) {
        White(0),
        Yellow(1),
        Blue(2),
        Invalid(Byte.MAX_VALUE);

        companion object {
            private val reverseValues: Map<Byte, Color> = values().associateBy { it.value }
            fun valueFrom(i: Byte) = reverseValues[i] ?: Invalid
        }
    }
}
