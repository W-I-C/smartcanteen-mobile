package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "CartMeals",
    foreignKeys=[
        ForeignKey(entity = Cart::class, parentColumns = arrayOf("cartId"), childColumns = arrayOf("cartId"))
    ]
)
data class CartMeals(
    @PrimaryKey val cartMealId:String,
    @ColumnInfo(name = "mealId") val mealId: String,
    @ColumnInfo(name = "cartId") val cartId: String,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "mealPrice") val mealPrice: Double,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "canTakeaway") val canTakeaway: Boolean,
)