package com.zakss.jkt48universe

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*
import android.graphics.Typeface

class MainActivity : Activity() {

    private val pink = Color.rgb(225, 65, 145)
    private val dark = Color.rgb(45, 25, 43)
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = dark
        window.navigationBarColor = dark
        showIntro()
    }

    private fun showIntro() {
        val videoId = resources.getIdentifier(
            "intro", "raw", packageName
        )

        if (videoId == 0) {
            showHome()
            return
        }

        val layout = FrameLayout(this)
        layout.setBackgroundColor(Color.BLACK)

        val video = VideoView(this)
        layout.addView(video, FrameLayout.LayoutParams(
            -1, -1
        ))

        val progress = ProgressBar(
            this, null,
            android.R.attr.progressBarStyleHorizontal
        )

        val params = FrameLayout.LayoutParams(
            -1, 8,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        )
        params.setMargins(35, 0, 35, 35)
        layout.addView(progress, params)

        setContentView(layout)

        video.setVideoURI(
            android.net.Uri.parse("android.resource://$packageName/$videoId")
        )

        video.setOnPreparedListener {
            progress.max = video.duration.coerceAtLeast(1)
            video.start()
            updateProgress(video, progress)
        }

        video.setOnCompletionListener {
            handler.removeCallbacksAndMessages(null)
            showHome()
        }

        video.setOnErrorListener { _, _, _ ->
            handler.removeCallbacksAndMessages(null)
            showHome()
            true
        }
    }

    private fun updateProgress(video: VideoView, bar: ProgressBar) {
        handler.post(object : Runnable {
            override fun run() {
                if (video.isPlaying) {
                    bar.progress = video.currentPosition
                    handler.postDelayed(this, 250)
                }
            }
        })
    }

    private fun showHome() {
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(22, 28, 22, 18)
        root.setBackgroundColor(Color.rgb(255, 244, 250))

        val title = TextView(this).apply {
            text = "JKT48 Universe 💗"
            textSize = 27f
            setTextColor(dark)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }

        root.addView(title)

        val subtitle = TextView(this).apply {
            text = "Your world of idols"
            textSize = 14f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 24)
        }
        root.addView(subtitle)

        val scroll = ScrollView(this)
        val menu = LinearLayout(this)
        menu.orientation = LinearLayout.VERTICAL

        val pages = listOf(
            "Home",
            "Members",
            "Events",
            "Chat",
            "Quiz",
            "Gallery",
            "News"
        )

        pages.forEach { page ->
            val button = Button(this).apply {
                text = page
                textSize = 17f
                setTextColor(Color.WHITE)
                setBackgroundColor(pink)
                isAllCaps = false
                setOnClickListener { showPage(page) }
            }

            val buttonParams = LinearLayout.LayoutParams(
                -1, 58
            )
            buttonParams.setMargins(0, 7, 0, 7)
            menu.addView(button, buttonParams)
        }

        scroll.addView(menu)
        root.addView(
            scroll,
            LinearLayout.LayoutParams( -1, 0, 1f)
        )

        val footer = TextView(this).apply {
            text = "Unofficial Fan-Made App"
            textSize = 11f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, 12, 0, 0)
        }
        root.addView(footer)

        setContentView(root)
    }

    private fun showPage(page: String) {
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(22, 28, 22, 22)
        root.setBackgroundColor(Color.rgb(255, 244, 250))

        val title = TextView(this).apply {
            text = page
            textSize = 27f
            setTextColor(dark)
            typeface = Typeface.DEFAULT_BOLD
        }
        root.addView(title)

        val description = when (page) {
            "Home" -> "Selamat datang di JKT48 Universe!"
            "Members" -> "Daftar member dan trainee akan tersedia di sini."
            "Events" -> "Jadwal show dan event akan tersedia di sini."
            "Chat" -> "Ruang percakapan karakter virtual akan tersedia di sini."
            "Quiz" -> "Uji pengetahuanmu tentang JKT48!"
            "Gallery" -> "Galeri foto akan tersedia di sini."
            "News" -> "Berita dan informasi akan tersedia di sini."
            else -> ""
        }

        val text = TextView(this).apply {
            this.text = description
            textSize = 16f
            setTextColor(dark)
            setPadding(0, 24, 0, 24)
        }
        root.addView(text)

        val back = Button(this).apply {
            this.text = "Kembali ke Home"
            isAllCaps = false
            setOnClickListener { showHome() }
        }
        root.addView(back)

        setContentView(root)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
