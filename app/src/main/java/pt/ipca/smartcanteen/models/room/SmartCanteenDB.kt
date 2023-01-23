package pt.ipca.smartcanteen.models.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pt.ipca.smartcanteen.models.room.dao.*
import pt.ipca.smartcanteen.models.room.tables.*


@Database(
    entities = [Profile::class, Meals::class, Tickets::class, Cart::class, CartMeals::class, CartMealsChanges::class],
    version = 4
)
abstract class SmartCanteenDB : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun mealsDao(): MealsDao
    abstract fun ticketsDao(): TicketsDao
    abstract fun cartDao(): CartDao
    abstract fun cartMealsDao(): CartMealsDao
    abstract fun cartMealsChangesDao(): CartMealsChangesDao

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