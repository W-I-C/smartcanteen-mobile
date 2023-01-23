package pt.ipca.smartcanteen.models.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Profile")
data class Profile(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "campusId") val campusId: String,
    @ColumnInfo(name = "campusName") val campusName: String,
    @ColumnInfo(name = "barId") val barId: String,
    @ColumnInfo(name = "barName") val barName: String,
)