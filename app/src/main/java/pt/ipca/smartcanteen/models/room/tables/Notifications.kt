package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Notifications")
data class Notifications(
    @PrimaryKey val notiId: String,
    @ColumnInfo(name = "receiverId") val barId: String,
    @ColumnInfo(name = "senderId") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "isSeen") val preparationTime: Int,
    @ColumnInfo(name = "isTradeProposal") val canTakeAway: Boolean,
    @ColumnInfo(name = "date") val price: Float,
    @ColumnInfo(name = "tradeId") val isDeleted: Boolean?
)