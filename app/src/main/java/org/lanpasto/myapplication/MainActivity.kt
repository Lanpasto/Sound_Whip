package org.lanpasto.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var imageRotator: ImageRotator
    private lateinit var button: Button
    private lateinit var shakeHandler: ShakeHandler
    private  val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.whipButton)
        imageView = findViewById(R.id.whipImage)

        button.setOnClickListener(clickListener)

        imageRotator = ImageRotator(this, imageView)

        shakeHandler = ShakeHandler(this, imageRotator)
    }

    override fun onResume() {
        super.onResume()
        shakeHandler.register()
        Log.d(TAG, "ShakeHandler registered")
    }

    override fun onPause() {
        super.onPause()
        shakeHandler.unregister()
        Log.d(TAG, "ShakeHandler unregistered")
    }

    private val clickListener = View.OnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - shakeHandler.lastClickTime > 1000) {
            shakeHandler.lastClickTime = currentTime
            shakeHandler.playSoundButton()
            imageRotator.rotate()
            Log.d(TAG, "Button clicked")
        }
    }
}







