package pt.ipca.smartcanteen.models.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ipca.smartcanteen.models.room.dao.MealsDao
import pt.ipca.smartcanteen.models.room.dao.ProfileDao
import pt.ipca.smartcanteen.models.room.tables.Meals
import pt.ipca.smartcanteen.models.room.tables.Profile


@Database(
    entities = [Profile::class, Meals::class],
    version = 2
)
abstract class SmartCanteenDB : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun mealsDao(): MealsDao

    companion object {
        @Volatile
        private var instance: SmartCanteenDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            SmartCanteenDB::class.java, "smartcanteen.db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build();

    }
}