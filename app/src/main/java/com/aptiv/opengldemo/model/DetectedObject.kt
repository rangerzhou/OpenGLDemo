package com.aptiv.adas.core.model

data class DetectedObject(
    val id: Int,
    val longitudinalPosition: Float, // 纵向位置
    val lateralPosition: Float, // 横向位置
    val velocity: Float, // 矢量速度
    val heading: Float,
    val length: Float,
    val width: Float,
    val height: Float,
    val type: Type
) {
    enum class Type(val value: Byte) {
        Undetermined(0),
        Car(1),
        Motorcycle(2),
        Truck(3),
        Pedestrian(4),
        Animal(7),
        Bicycle(9),
        UnidentifiedVehicle(10);

        companion object {
            private val reverseValues: Map<Byte, Type> = values().associateBy { it.value }
            fun valueFrom(i: Byte) = reverseValues[i] ?: Undetermined
        }
    }
}
