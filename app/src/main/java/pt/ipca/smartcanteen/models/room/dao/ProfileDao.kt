package pt.ipca.smartcanteen.models.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.ipca.smartcanteen.models.room.tables.Profile

@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile")
    fun getProfile(): Profile

    @Insert
    fun insertAll(vararg profile: Profile)
}