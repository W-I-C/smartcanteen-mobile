package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "AllowedChanges")
data class AllowedChanges(
    @PrimaryKey val changeId: String,
    @ColumnInfo(name = "mealId") val name: String,
    @ColumnInfo(name = "ingName") val uid: String,
    @ColumnInfo(name = "ingDosage") val date: String,
    @ColumnInfo(name = "isRemoveOnly") val amount: String,
    @ColumnInfo(name = "canBeIncremented") val canBeIncremented: Boolean,
    @ColumnInfo(name = "canBeDecremented") val canBeDecremented: Boolean,
    @ColumnInfo(name = "incrementLimit") val incrementLimit: Int,
    @ColumnInfo(name = "decrementLimit") val decrementLimit: Int,
    @ColumnInfo(name = "defaultValue") val defaultValue: Int
)