package pt.ipca.smartcanteen.models.room.dao

import androidx.room.*
import pt.ipca.smartcanteen.models.room.tables.Cart

@Dao
interface CartDao {
    @Query("SELECT * FROM Cart")
    fun getAllCarts(): List<Cart>

    @Query("SELECT * FROM Cart WHERE cartId=:cartId")
    fun getCart(vararg cartId: String): Cart?

    @Insert
    fun insertAll(vararg Cart: Cart)

    @Update
    fun update(vararg Cart: Cart)

    @Delete
    fun delete(vararg Cart: Cart)
}