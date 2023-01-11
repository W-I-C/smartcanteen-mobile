package pt.ipca.smartcanteen

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
    }
}