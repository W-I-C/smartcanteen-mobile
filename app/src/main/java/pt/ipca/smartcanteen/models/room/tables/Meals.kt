package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Meals")
data class Meals(
    @PrimaryKey val mealId: String,
    @ColumnInfo(name = "barId") val barId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "preparationTime") val preparationTime: Int,
    @ColumnInfo(name = "canTakeAway") val canTakeAway: Boolean,
    @ColumnInfo(name = "price") val price: Float,
    @ColumnInfo(name = "isDeleted") val isDeleted: Boolean,
    @ColumnInfo(name = "canBeMade") val canBeMade: Boolean,
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean
)