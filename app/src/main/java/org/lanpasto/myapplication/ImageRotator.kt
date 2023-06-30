package org.lanpasto.myapplication

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

import android.util.Log


class ImageRotator(private val context: Context, private val imageView: ImageView) {
    private val tagOfLog = "ImageRotator"
    private var animation: Animation? = null

    fun rotate() {
        animation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim)
        animation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                Log.d(tagOfLog, "Animation started")
            }

            override fun onAnimationEnd(animation: Animation?) {
                Log.d(tagOfLog, "Animation ended")
            }

            override fun onAnimationRepeat(animation: Animation?) {
                Log.d(tagOfLog, "Animation repeated")
            }
        })

        imageView.startAnimation(animation)
    }

    fun stopRotation() {
        animation?.cancel()
        Log.d(tagOfLog, "Animation stopped")
    }
}
