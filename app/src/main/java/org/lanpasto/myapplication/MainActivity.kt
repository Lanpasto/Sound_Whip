package org.lanpasto.myapplication

import ShakeDetector
import android.content.Context
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), ShakeDetector.Listener {
    private lateinit var imageView: ImageView
    private lateinit var imageRotator: ImageRotator
    private lateinit var button: Button
    private var lastClickTime: Long = 0
    private lateinit var shakeDetector: ShakeDetector

    private val clickListener = View.OnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > 1000) {
            lastClickTime = currentTime
            playSoundButton()
            imageRotator.rotate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.whipButton)
        imageView = findViewById(R.id.whipImage)

        button.setOnClickListener(clickListener)

        imageRotator = ImageRotator(this, imageView)

        // Initialize ShakeDetector
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector(sensorManager, this)
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.register()
        Log.d(TAG, "Shake detection registered")
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.unregister()
        Log.d(TAG, "Shake detection unregistered")
    }

    override fun onShakeStart() {
        Log.d(TAG, "Shake started")
    }

    override fun onShake() {
        playSoundShake()
        imageRotator.rotate()
        Log.d(TAG, "Shake detected")
    }

    override fun onShakeStop() {
        imageRotator.stopRotation()
        stopSoundShake()
        Log.d(TAG, "Shake stopped")
    }

    private fun playSoundShake() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.whip)
        mediaPlayer.start()
        Log.d(TAG, "Shake sound played")
    }

    private fun stopSoundShake() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.whip)
        mediaPlayer.stop()
        Log.d(TAG, "Shake sound stopped")
    }

    private fun playSoundButton() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.whip)
        mediaPlayer.start()

        Handler().postDelayed({
            mediaPlayer.release()
        }, 1000)

        Log.d(TAG, "Button sound played")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}


