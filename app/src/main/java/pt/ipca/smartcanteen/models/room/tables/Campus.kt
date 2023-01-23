package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Campus")
data class Campus(
    @PrimaryKey val campusId: String,
    @ColumnInfo(name = "name") val name: String,
)