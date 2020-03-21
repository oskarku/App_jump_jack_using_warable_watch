package pl.wylosujdrina.firstapp

import android.content.Context
import android.os.Vibrator
import android.preference.PreferenceManager
import java.util.prefs.Preferences

private const val DEFAULT_VIBRATION_DURATION_MS = 200 // in millis

private const val PREF_KEY_COUNTER = "counter"

/**
 * Causes device to vibrate for the given duration (in millis). If duration is set to 0, then it
 * will use the `DEFAULT_VIBRATION_DURATION_MS`.
 */
fun vibrate(context: Context, duration: Int) {
    var duration = duration
    if (duration == 0) {
        duration = DEFAULT_VIBRATION_DURATION_MS
    }
    val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    v.vibrate(duration.toLong())
}

/**
 * Saves the counter value in the preference storage. If `value`
 * is negative, then the value will be removed from the preferences.
 */
fun saveCounterToPreference(context: Context?, value: Int) {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    if (value < 0) { // we want to remove
        pref.edit().remove(PREF_KEY_COUNTER).apply()
    } else {
        pref.edit().putInt(PREF_KEY_COUNTER, value).apply()
    }
}

/**
 * Retrieves the value of counter from preference manager. If no value exists, it will return
 * `0`.
 */
fun getCounterFromPreference(context: Context?): Int {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    return pref.getInt(PREF_KEY_COUNTER, 0)
}