package pl.wylosujdrina.firstapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import androidx.wear.ambient.AmbientModeSupport
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity(), SensorEventListener {
//    / An up-down movement that takes more than 2 seconds will not be registered (in nanoseconds).

    private var TIME_THRESHOLD_NS = java.util.concurrent.TimeUnit.SECONDS.toNanos(2)
    private val HAND_DOWN_GRAVITY_X_THRESHOLD = -.040f
    private val HAND_UP_GRAVITY_X_THRESHOLD = -.010f
    private val TAG = "debug_pick_up"

    private var mSensorManager: SensorManager? = null
    private var mSensor: Sensor? = null
    private var mLastTime: Long = 0
    private var mJumpCounter = 0
    private var mHandDown = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager?.let { mSensor= mSensorManager!!.getDefaultSensor(Sensor.TYPE_GRAVITY)  }

        setAmbientEnabled()
    }

    override fun onResume() {
        super.onResume()
        if (mSensorManager?.let {it.registerListener(
                this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )  }!!
        ) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG,"udalo sie")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mSensorManager?.unregisterListener(this)

    }

    override fun onSensorChanged(event: SensorEvent) {
        detectJump(event.values[0], event.timestamp)
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) { // No op.
    }

    private fun detectJump(xGravity: Float, timestamp: Long) {
        if (xGravity <= HAND_DOWN_GRAVITY_X_THRESHOLD
            || xGravity >= HAND_UP_GRAVITY_X_THRESHOLD
        ) {
            if (timestamp - mLastTime < TIME_THRESHOLD_NS) { // Hand is down when yValue is negative.
                onJumpDetected(xGravity <= HAND_DOWN_GRAVITY_X_THRESHOLD)
            }
            mLastTime = timestamp
        }
    }


    private fun onJumpDetected(handDown: Boolean) {
        if (mHandDown != handDown) {
            mHandDown = handDown
            // Only count when the hand is down (means the hand has gone up, then down).
            if (mHandDown) {
                mJumpCounter++
                setCounter(mJumpCounter)
            }
        }
    }


    private fun setCounter(i: Int) {
        coubtPajacykTV.text = i.toString()
        coubtPajacykTV.invalidate()
        if (i > 0 && i % 10 == 0) {
            vibrate(this, 0)
        }
    }

    fun resetCounter() {
        setCounter(0)
    }


    fun getAmbientCallback(): AmbientModeSupport.AmbientCallback? {
        return MyAmbientCallback()
    }

    /** Customizes appearance for Ambient mode. (We don't do anything minus default.)  */
    private class MyAmbientCallback : AmbientModeSupport.AmbientCallback() {
        /** Prepares the UI for ambient mode.  */
        override fun onEnterAmbient(ambientDetails: Bundle) {
            super.onEnterAmbient(ambientDetails)
        }
        
        override fun onUpdateAmbient() {
            super.onUpdateAmbient()
        }

        /** Restores the UI to active (non-ambient) mode.  */
        override fun onExitAmbient() {
            super.onExitAmbient()
        }
    }

}

