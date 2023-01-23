package pt.ipca.smartcanteen.models.room.dao

import androidx.room.*
import pt.ipca.smartcanteen.models.room.tables.CartMealsChanges

@Dao
interface CartMealsChangesDao {
    @Query("SELECT * FROM CartMealsChanges WHERE cartMealId=:cartMealId")
    fun getAllMealChanges(vararg cartMealId:String): List<CartMealsChanges>

    @Query("SELECT * FROM CartMealsChanges WHERE cartChangeId=:cartMealChangeId")
    fun getMealChange(vararg cartMealChangeId:String): CartMealsChanges?

    @Insert
    fun insertAll(vararg cartMealsChanges: CartMealsChanges)

    @Update
    fun update(vararg cartMealsChanges: CartMealsChanges)

    @Delete
    fun delete(vararg cartMealsChanges: CartMealsChanges)
}