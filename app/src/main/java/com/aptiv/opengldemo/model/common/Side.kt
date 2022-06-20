package com.aptiv.adas.core.model.common

enum class Side(val value: Byte) {
    Unknown(0),
    Left(1),
    Right(2),
    Both(3),
    Invalid(Byte.MAX_VALUE);

    companion object {
        private val reverseValues: Map<Byte, Side> = values().associateBy { it.value }
        fun valueFrom(i: Byte) = reverseValues[i] ?: Invalid
    }
}
