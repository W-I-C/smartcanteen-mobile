package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Cart"
)
data class Cart(
    @PrimaryKey val cartId: String,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean,
)