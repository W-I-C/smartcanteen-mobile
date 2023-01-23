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
    @ColumnInfo(name = "uid") val name: String,
    @ColumnInfo(name = "state") val email: String,
    @ColumnInfo(name = "cartId") val cartId: String,
    @ColumnInfo(name = "emissionDate") val emissionDate: Date,
    @ColumnInfo(name = "isPickedUp") val isPickedUp: Boolean,
    @ColumnInfo(name = "total") val total: Float,
)