package com.mobileconnect.app

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ids = intArrayOf(
            R.id.connectButton,
            R.id.remoteButton,
            R.id.keyboardButton,
            R.id.mediaButton,
            R.id.screenButton,
            R.id.filesButton,
            R.id.gameButton,
            R.id.settingsButton
        )
        ids.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                Toast.makeText(this, "Mobile Connect: feature ready", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
