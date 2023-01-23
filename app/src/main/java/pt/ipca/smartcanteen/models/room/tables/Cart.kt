package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Cart")
data class Cart(
    @PrimaryKey val ticketid: String,
    @ColumnInfo(name = "cartId") val name: String,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean,
)