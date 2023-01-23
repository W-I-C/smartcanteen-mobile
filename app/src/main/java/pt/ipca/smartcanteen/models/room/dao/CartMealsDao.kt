package pt.ipca.smartcanteen.models.room.dao

import androidx.room.*
import pt.ipca.smartcanteen.models.room.tables.CartMeals
@Dao
interface CartMealsDao {
    @Query("SELECT * FROM CartMeals WHERE cartId=:cartId")
    fun getAllCartMeals(vararg cartId:String): List<CartMeals>

    @Query("SELECT * FROM CartMeals WHERE cartMealId=:cartMealId")
    fun getCartMeal(vararg cartMealId:String): CartMeals?

    @Insert
    fun insertAll(vararg cartMeals: CartMeals)

    @Update
    fun update(vararg cartMeals: CartMeals)

    @Delete
    fun delete(vararg cartMeals: CartMeals)
}