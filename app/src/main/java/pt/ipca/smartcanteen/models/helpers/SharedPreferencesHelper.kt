package pt.ipca.smartcanteen.models.helpers

import android.content.Context
import android.content.SharedPreferences

import pt.ipca.smartcanteen.R

object SharedPreferencesHelper {
    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

}