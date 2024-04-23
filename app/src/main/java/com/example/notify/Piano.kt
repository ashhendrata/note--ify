package com.example.notify

import android.util.Log


import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent


class Piano : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    private val password = arrayOf(
        R.id.keyB6, R.id.keyA6Sharp, R.id.keyG5
    )

    private var passwordAttempt: Array<Int> = arrayOf()

    private val checkHandler = Handler(Looper.getMainLooper())
    private val checkInterval: Long = 1000 // Checking interval in milliseconds

    private val whiteKeyIds = arrayOf(
        R.id.keyC5, R.id.keyD5, R.id.keyE5, R.id.keyF5, R.id.keyG5,
        R.id.keyA6, R.id.keyB6
    )

    private val blackKeyIds = arrayOf(
        R.id.keyC5Sharp, R.id.keyD5Sharp, R.id.keyF5Sharp, R.id.keyG5Sharp,
        R.id.keyA6Sharp
    )

    private val whiteKeySounds = arrayOf(
        R.raw.c5, R.raw.d5, R.raw.e5, R.raw.f5, R.raw.g5,
        R.raw.a6, R.raw.b6
    )

    private val blackKeySounds = arrayOf(
        R.raw.c44, R.raw.d44, R.raw.f44, R.raw.g44,
        R.raw.a55, R.raw.c55, R.raw.d55, R.raw.f55, R.raw.g55,
        R.raw.a66
    )

    private val clearHandler = Handler(Looper.getMainLooper())
    private val clearRunnable = Runnable {
        passwordAttempt = arrayOf() // This will clear the array
    }


    private lateinit var keys: Array<Button>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //highlight
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        setContentView(R.layout.activity_main)
        startPasswordCheck()

        val scrollView = findViewById<HorizontalScrollView>(R.id.scrollView) //highlight
        scrollView.post { scrollView.scrollTo((scrollView.getChildAt(0).width * 0.55).toInt(), 0) }

        scrollView.setOnTouchListener { _, _ -> true }


        val whiteKeys = whiteKeyIds.mapIndexed { index, keyId ->
            findViewById<Button>(keyId).apply {
                setOnClickListener {
                    setBackgroundColor(Color.parseColor("#80ffe5"))
                    playSound(whiteKeySounds[index])
                    passwordAttempt += keyId

                    // Reset the delay each time a key is pressed
                    resetPasswordAttemptDelay()

                    Handler().postDelayed({
                        setBackgroundColor(Color.WHITE)
                    }, 100)
                }
            }
        }

        val blackKeys = blackKeyIds.mapIndexed { index, keyId ->
            findViewById<Button>(keyId).apply {
                setOnClickListener {
                    setBackgroundColor(Color.parseColor("#80ffe5"))
                    playSound(blackKeySounds[index])
                    passwordAttempt += keyId

                    // Reset the delay each time a key is pressed
                    resetPasswordAttemptDelay()

                    Handler().postDelayed({
                        setBackgroundColor(Color.BLACK)
                    }, 100)
                }
            }
        }

        keys = whiteKeys.toTypedArray() + blackKeys.toTypedArray()
    }

    private fun resetPasswordAttemptDelay() {
        // Cancel any pending posts of clearRunnable and post a new one
        clearHandler.removeCallbacks(clearRunnable)
        clearHandler.postDelayed(clearRunnable, 3000) // 7 seconds expressed in milliseconds
    }


    private fun playSound(soundResource: Int) {
        mediaPlayer = MediaPlayer.create(this, soundResource)
        mediaPlayer.start()


        mediaPlayer.setOnCompletionListener {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                it.release()
            }, 1000) // Stop playback after 1 second
        }
    }

    private fun startPasswordCheck() {
        checkHandler.postDelayed(object : Runnable {
            override fun run() {
                checkPassword()
                checkHandler.postDelayed(this, checkInterval)
            }
        }, checkInterval)
    }

    private fun checkPassword() {
        if (passwordAttempt.contentEquals(password)) {
            Log.d("PasswordCheck", "Correct")
            passwordAttempt = arrayOf() // Reset password attempt after correct entry

            // Create an intent to start NextActivity
            val intent = Intent(this, MyCircle::class.java)
            startActivity(intent)

            // Optionally, you might want to finish the current activity
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
