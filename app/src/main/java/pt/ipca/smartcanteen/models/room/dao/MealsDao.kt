package pt.ipca.smartcanteen.models.room.dao

import androidx.room.*
import pt.ipca.smartcanteen.models.room.tables.Meals

@Dao
interface MealsDao {
    @Query("SELECT * FROM Meals")
    fun getAllMeals(): List<Meals>

    @Query("SELECT * FROM Meals WHERE mealId=:mealId")
    fun getMeal(vararg mealId: String): Meals?

    @Insert
    fun insertAll(vararg profile: Meals)

    @Update
    fun update(vararg profile: Meals)

    @Delete
    fun delete(vararg profile: Meals)
}