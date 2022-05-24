package com.aptiv.opengldemo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        setContentView(R.layout.activity_main)
    }


    private fun initUI() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // 保持常亮
        // 备用：隐藏状态栏导航栏，结合 style.xml 一起使用效果更好，否则状态栏和导航栏是启动后先显示再隐藏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.hide(WindowInsets.Type.systemBars())
        } else {
            ViewCompat.getWindowInsetsController(window.decorView)
                ?.hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}