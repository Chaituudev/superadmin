package project.shared

import android.content.Context

object PrefManager {
    private const val PREF_NAME = "MyAppPrefs"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    private const val KEY_ROLE = "userRole"

    fun setLogin(context: Context, role: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_ROLE, role)
            apply()
        }
    }

    fun logout(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getRole(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_ROLE, null)
    }
}
