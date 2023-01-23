package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "CartMealsChanges",
    foreignKeys=[
        ForeignKey(entity = CartMeals::class, parentColumns = arrayOf("cartMealId"), childColumns = arrayOf("cartMealId"))
    ]
)
data class CartMealsChanges(
    @PrimaryKey val cartChangeId :String,
    @ColumnInfo(name = "cartMealId") val cartMealId: String,
    @ColumnInfo(name = "ingName") val ingName: String,
    @ColumnInfo(name = "ingAmount") val ingAmount: Int,
    @ColumnInfo(name = "isRemoveOnly") val isRemoveOnly: Boolean,
    @ColumnInfo(name = "canBeIncremented") val canBeIncremented: Boolean,
    @ColumnInfo(name = "canBeDecremented") val canBeDecremented: Boolean,
)