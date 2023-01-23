package pt.ipca.smartcanteen.models.helpers

import android.content.Context
import androidx.room.Room
import pt.ipca.smartcanteen.models.room.SmartCanteenDB

object SmartCanteenDBHelper {
    fun getInstance(applicationContext: Context):SmartCanteenDB{
        return Room.databaseBuilder(
            applicationContext,
            SmartCanteenDB::class.java, "smartcanteen.db"
        ).build()
    }
}