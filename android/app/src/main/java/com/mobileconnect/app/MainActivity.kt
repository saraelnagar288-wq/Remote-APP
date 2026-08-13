package com.mobileconnect.app

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var ip: EditText
    private lateinit var pin: EditText
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        val title = TextView(this).apply { text = "📱 Mobile Connect"; textSize = 28f }
        status = TextView(this).apply { text = "● Disconnected"; textSize = 16f }
        ip = EditText(this).apply { hint = "PC IP (e.g. 192.168.1.10)" }
        pin = EditText(this).apply { hint = "Pairing PIN"; inputType = 2 }
        val pair = Button(this).apply { text = "Connect & Pair" }
        val remote = Button(this).apply { text = "🖱️ Remote Touchpad" }
        val keyboard = Button(this).apply { text = "⌨️ Keyboard" }
        val media = Button(this).apply { text = "🎵 Play / Pause" }
        val files = Button(this).apply { text = "📁 File Transfer" }
        val screen = Button(this).apply { text = "📺 Screen Mirroring" }
        val game = Button(this).apply { text = "🎮 Game Remote" }

        listOf(title, status, ip, pin, pair, remote, keyboard, media, files, screen, game).forEach(root::addView)
        setContentView(root)

        pair.setOnClickListener {
            thread {
                val ok = post("/pair", "{\"pin\":\"${pin.text}\"}")
                runOnUiThread { status.text = if (ok) "● Paired / Connected ✓" else "● Pairing failed" }
            }
        }
        remote.setOnClickListener { showRemoteControls() }
        keyboard.setOnClickListener { send("keyboard", "${pin.text}") }
        media.setOnClickListener { send("media", "play_pause") }
        files.setOnClickListener { Toast.makeText(this, "File transfer module ready for PC companion", Toast.LENGTH_SHORT).show() }
        screen.setOnClickListener { Toast.makeText(this, "Screen streaming requires the PC companion", Toast.LENGTH_SHORT).show() }
        game.setOnClickListener { showGameRemote() }
    }

    private fun showRemoteControls() {
        val box = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(32, 16, 32, 16) }
        val move = EditText(this).apply { hint = "dx,dy" }
        box.addView(move)
        AlertDialog.Builder(this).setTitle("Remote Touchpad").setView(box)
            .setPositiveButton("Click") { _, _ -> send("mouse_click", "left") }
            .setNegativeButton("Send Move") { _, _ -> send("mouse_move", move.text.toString()) }.show()
    }

    private fun showGameRemote() {
        val keys = arrayOf("W", "A", "S", "D", "Space", "Shift", "Ctrl", "E", "R", "F")
        AlertDialog.Builder(this).setTitle("🎮 Game Remote").setItems(keys) { _, which -> send("key", keys[which]) }.show()
    }

    private fun send(type: String, value: String) {
        thread { post("/command", "{\"command\":{\"type\":\"$type\",\"value\":\"${value.replace("\"", "\\\"")}\"}}") }
    }

    private fun post(path: String, body: String): Boolean = try {
        val host = ip.text.toString().trim()
        val c = URL("http://$host:8765$path").openConnection() as HttpURLConnection
        c.requestMethod = "POST"
        c.doOutput = true
        c.connectTimeout = 4000
        c.readTimeout = 4000
        c.setRequestProperty("Content-Type", "application/json")
        c.outputStream.use { it.write(body.toByteArray()) }
        c.responseCode in 200..299
    } catch (_: Exception) { false }
}
