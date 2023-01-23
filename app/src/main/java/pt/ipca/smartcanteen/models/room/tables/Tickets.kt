package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "Tickets",
    foreignKeys=[
        ForeignKey(entity = Cart::class, parentColumns = arrayOf("cartId"), childColumns = arrayOf("cartId"))
    ]
)
data class Tickets(
    @PrimaryKey val ticketid: String,
    @ColumnInfo(name = "barName") val barname: String,
    @ColumnInfo(name = "ownerName") val ownername: String,
    @ColumnInfo(name = "stateName") val stateName: String,
    @ColumnInfo(name = "cartId") val cartId: String,
    @ColumnInfo(name = "emissionDate") val emissionDate: String,
    @ColumnInfo(name = "pickupTime") val pickupTime: String,
    @ColumnInfo(name = "ticketAmount") val ticketAmount: Int,
    @ColumnInfo(name = "total") val total: Float,
    @ColumnInfo(name = "nEncomenda") val nEncomenda: Int,
    @ColumnInfo(name = "isFree") val isFree: Boolean
)