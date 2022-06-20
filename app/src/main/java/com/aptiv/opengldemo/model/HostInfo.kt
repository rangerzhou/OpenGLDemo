package com.aptiv.adas.core.model

data class HostInfo(
    val speed: Float,
    val yawRate: Float, // 偏航率
    val steeringWheelAngle: Float, // 方向盘角度
    val gear: Gear,
    val turnIndication: TurnIndication, // 转向指示
    val isBrakePedalActive: Boolean // 刹车踏板激活
) {

    val speedKmH = speed * 3.6f

    fun isValid(): Boolean {
        return gear != Gear.Invalid && turnIndication != TurnIndication.Invalid
                && speedKmH >= -30 && speedKmH <= 200
    }

    enum class Gear(val value: Byte) {
        Park(0),
        Reverse(1),
        Neutral(2),
        Drive(3),
        Invalid(Byte.MAX_VALUE);

        companion object {
            private val reverseValues: Map<Byte, Gear> = values().associateBy { it.value }
            fun valueFrom(i: Byte) = reverseValues[i] ?: Invalid
        }
    }

    enum class TurnIndication(val value: Byte) {
        Neutral(0),
        Left(1),
        Right(2),
        Invalid(Byte.MAX_VALUE);

        companion object {
            private val reverseValues: Map<Byte, TurnIndication> = values().associateBy { it.value }
            fun valueFrom(i: Byte) = reverseValues[i] ?: Invalid
        }
    }
}
