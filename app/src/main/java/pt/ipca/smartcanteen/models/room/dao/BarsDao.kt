package pt.ipca.smartcanteen.models.room.dao

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import pt.ipca.smartcanteen.models.room.tables.Bars

interface BarsDao {
    @Query("SELECT * FROM Bars")
    fun getBars(): Bars?

    @Insert
    fun insertAll(vararg profile: Bars)

    @Update
    fun update(vararg profile: Bars)
}