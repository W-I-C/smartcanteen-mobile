package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Bars",
    foreignKeys = [
        ForeignKey(entity = Campus::class, parentColumns = arrayOf("campusId"), childColumns = arrayOf("campusId"))
    ]
)
class Bars(
    @PrimaryKey val barId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "campusId") val campusId: String,
)