import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import java.util.concurrent.PriorityBlockingQueue

@Suppress("NAME_SHADOWING")
class ShakeDetector(private val sensorManager: SensorManager, private val listener: Listener) :
    SensorEventListener {

    interface Listener {
        fun onShakeStart()
        fun onShake()
        fun onShakeStop()
    }

    companion object {
        private const val TAG = "ShakeDetector"
        private const val DETECTION_INTERVAL = 500
        private const val SHAKE_FORCE = 2.5f
        private const val SHAKE_PERCENT = 0.1
    }

    private var accelerometer: Sensor? = null
    private val sensitivityThreshold: Float = SHAKE_FORCE
    private var isShaking: Boolean = false
    private var lastCheck: Long = 0
    private val sensorDataQueue: PriorityBlockingQueue<SensorData> = PriorityBlockingQueue()
    private val sensorDataArrayList: ArrayList<SensorData> = ArrayList()

    init {
        register()
    }

    fun register() {
        if (accelerometer != null) {
            return
        }

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (accelerometer != null) {
            isShaking = false
            sensorDataQueue.clear()
            sensorDataArrayList.clear()
            sensorManager.registerListener(
                this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun unregister() {
        if (accelerometer != null) {
            sensorManager.unregisterListener(this, accelerometer)
            accelerometer = null
            sensorDataQueue.clear()
            sensorDataArrayList.clear()
        }
        isShaking = false
    }

    fun onDestroy() {
        // Leave it empty. The system will automatically release the resources used by these objects.
    }

    private fun isShaking(event: SensorEvent): Boolean {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val magnitudeSquared =
            (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)
        return magnitudeSquared > sensitivityThreshold
    }

    private fun evaluateSensorData(sensorData: ArrayList<SensorData>) {
        if (sensorData.isNotEmpty()) {
            val total = sensorData.size.toDouble()
            var shaking = 0.0
            for (i in sensorData.indices) {
                val sensorData = sensorData[i]
                if (sensorData.isShaking()) {
                    shaking++
                }
            }

            val shakePercent = shaking / total
            Log.d(TAG, "SHAKING: $shaking TOTAL: $total %: $shakePercent")
            if (shakePercent > SHAKE_PERCENT) {
                if (!isShaking) {
                    isShaking = true
                    listener.onShakeStart()
                    Log.d(TAG, "Shake started")
                }

                listener.onShake()
                Log.d(TAG, "Shake in progress")
            } else {
                if (isShaking) {
                    isShaking = false
                    listener.onShakeStop()
                    Log.d(TAG, "Shake stopped")
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val isShaking = isShaking(event)
            val timestamp = event.timestamp

            val sensorData = SensorData(isShaking, timestamp)
            sensorDataQueue.add(sensorData)

            val current = System.currentTimeMillis()

            if (lastCheck == 0L) {
                lastCheck = current
            }

            if (current - lastCheck > DETECTION_INTERVAL) {
                lastCheck = current

                sensorDataArrayList.clear()
                sensorDataQueue.drainTo(sensorDataArrayList)

                evaluateSensorData(sensorDataArrayList)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Don't care
    }

    private class SensorData(private val isShaking: Boolean, private val timestamp: Long) :
        Comparable<SensorData> {

        fun isShaking(): Boolean {
            return isShaking
        }

        override fun compareTo(other: SensorData): Int {
            return when {
                timestamp > other.timestamp -> 1
                timestamp == other.timestamp -> 0
                else -> -1
            }
        }
    }
}

