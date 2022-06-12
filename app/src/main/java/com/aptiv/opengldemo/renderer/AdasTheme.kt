package com.aptiv.opengldemo.renderer

data class AdasTheme(
    val hostCarColor: String,
    val detectedObjectColor: String,
    val targetObjectColor: String,
    val warningColor: String,
    val alertColor: String,
    val shadowColor: String
) {

    companion object {
        val THEME = AdasTheme("#FFFFFF",
            "#707D82", "#FFB300",
            "#FFB300", "#BF002B", "#000000")
    }
}
