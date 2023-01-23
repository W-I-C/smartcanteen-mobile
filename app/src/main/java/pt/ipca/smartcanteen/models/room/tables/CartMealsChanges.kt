package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "CartMealsChanges")
data class CartMealsChanges(
    @ColumnInfo(name = "cartMealId") val name: String,
    @ColumnInfo(name = "changeId") val changeId: String,
    @ColumnInfo(name = "amount") val amount: String,
)