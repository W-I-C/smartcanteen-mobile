package pt.ipca.smartcanteen.models.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pt.ipca.smartcanteen.models.room.tables.Profile

@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile")
    fun getProfile(): Profile

    @Insert
    fun insertAll(vararg profile: Profile)

    @Update
    fun update(vararg profile: Profile)
}