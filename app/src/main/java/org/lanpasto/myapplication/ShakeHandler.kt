package org.lanpasto.myapplication

import ShakeDetector
import android.content.Context
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.util.Log

class ShakeHandler(private val context: Context, private val imageRotator: ImageRotator) :
    ShakeDetector.Listener {
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val shakeDetector: ShakeDetector = ShakeDetector(sensorManager, this)
    private var mediaPlayer: MediaPlayer? = null
    var lastClickTime: Long = 0
    private var shakeStartTime: Long = 0
    private  val TAG = "ShakeHandler"
    fun register() {
        shakeDetector.register()
    }

    fun unregister() {
        shakeDetector.unregister()
    }

    override fun onShakeStart() {
        shakeStartTime = System.currentTimeMillis()
        Log.d(TAG, "Shake started")
    }

    override fun onShake() {
        val currentTime = System.currentTimeMillis()

        if (shakeStartTime == 0L) {
            shakeStartTime = currentTime
        }

        val elapsedTime = currentTime - shakeStartTime

        if (elapsedTime >= 15000) {
            stopSoundShake()
            playSoundShakeLong()
        } else {
            playSoundShakePlash()
        }
        imageRotator.rotate()

        Log.d(TAG, "Shake detected")
    }

    override fun onShakeStop() {
        stopSoundShakeLong()
        playSoundShakeWhip()

        Log.d(TAG, "Shake stopped")
    }

    private fun playSoundShakeWhip() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.whip)
        mediaPlayer.start()
        Log.d(TAG, "Playing whip sound")
    }

    private fun stopSoundShakeLong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        Log.d(TAG, "Stopping long shake sound")
    }

    private fun playSoundShakePlash() {
        mediaPlayer = MediaPlayer.create(context, R.raw.whiplash)
        mediaPlayer?.start()

        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }

        Log.d(TAG, "Playing plash sound")
    }

    private fun playSoundShakeLong() {
        mediaPlayer = MediaPlayer.create(context, R.raw.whip_long_shake)
        mediaPlayer?.start()

        Log.d(TAG, "Playing long shake sound")
    }

    private fun stopSoundShake() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        Log.d(TAG, "Stopping shake sound")
    }

    fun playSoundButton() {
        mediaPlayer = MediaPlayer.create(context, R.raw.whip)
        mediaPlayer?.start()

        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }

        Log.d(TAG, "Playing button sound")
    }
}
