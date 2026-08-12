package com.mobileconnect.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = TextView(this).apply {
            text = "Mobile Connect\n\nAndroid 16 project initialized."
            textSize = 22f
            setPadding(48, 48, 48, 48)
        }
        setContentView(view)
    }
}
