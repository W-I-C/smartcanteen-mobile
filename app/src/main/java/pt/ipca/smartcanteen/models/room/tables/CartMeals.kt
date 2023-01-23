package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "CartMeals")
data class CartMeals(
    @PrimaryKey val ticketid: String,
    @ColumnInfo(name = "cartMealId") val name: String,
    @ColumnInfo(name = "mealId") val uid: String,
    @ColumnInfo(name = "cartId") val date: Date,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "mealPrice") val mealPrice: Float,
    @ColumnInfo(name = "insertTime") val insertTime: Date
)